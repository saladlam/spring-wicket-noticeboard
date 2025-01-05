package info.saladlam.example.spring.noticeboard.repository;

import java.time.LocalDateTime;
import java.util.List;

import info.saladlam.example.spring.noticeboard.entity.Message;

public interface MessageRepository {

	List<Message> findPublished(LocalDateTime at);

	List<Message> findWaitingApprove();

	List<Message> findByOwner(String owner);

	Message findOne(long id);

	Message save(Message message);

}
