package info.saladlam.example.spring.noticeboard.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import info.saladlam.example.spring.noticeboard.entity.Message;

@Repository
public class JdbcMessageRepository implements MessageRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Timestamp toTimestamp(LocalDateTime datetime) {
		if (Objects.isNull(datetime)) {
			return null;
		} else {
			return Timestamp.valueOf(datetime);
		}
	}

	@Override
	public List<Message> findPublished(LocalDateTime at) {
		return this.jdbcTemplate.query(
				"SELECT * FROM message WHERE approved_by IS NOT NULL AND publish_date <= ? AND (remove_date IS NULL OR remove_date > ?) ORDER BY publish_date DESC",
				BeanPropertyRowMapper.newInstance(Message.class), at, at);
	}

	@Override
	public List<Message> findWaitingApprove() {
		return this.jdbcTemplate.query("SELECT * FROM message WHERE approved_by IS NULL ORDER BY publish_date DESC",
				BeanPropertyRowMapper.newInstance(Message.class));
	}

	@Override
	public List<Message> findByOwner(String owner) {
		return this.jdbcTemplate.query("SELECT * FROM message WHERE owner = ? ORDER BY publish_date DESC",
				BeanPropertyRowMapper.newInstance(Message.class), owner);
	}

	@Override
	public Message findOne(long id) {
		return this.jdbcTemplate.queryForObject("SELECT * FROM message WHERE id = ?",
				BeanPropertyRowMapper.newInstance(Message.class), id);
	}

	@Override
	public Message save(Message message) {
		if (message.getId() == null) {
			String sql = "INSERT INTO message(publish_date, remove_date, owner, description, approved_by, approved_date) VALUES (?, ?, ?, ?, ?, ?)";
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setTimestamp(1, this.toTimestamp(message.getPublishDate()));
				ps.setTimestamp(2, this.toTimestamp(message.getRemoveDate()));
				ps.setString(3, message.getOwner());
				ps.setString(4, message.getDescription());
				ps.setString(5, message.getApprovedBy());
				ps.setTimestamp(6, this.toTimestamp(message.getApprovedDate()));
				return ps;
			}, keyHolder);

			message.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
		} else {
			String sql = "UPDATE message SET publish_date = ?, remove_date = ?, owner = ?, description = ?, approved_by = ?, approved_date = ? WHERE id = ?";
			this.jdbcTemplate.update(sql, message.getPublishDate(), message.getRemoveDate(), message.getOwner(),
					message.getDescription(), message.getApprovedBy(), message.getApprovedDate(), message.getId());
		}

		return message;
	}

}
