package info.saladlam.example.spring.noticeboard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.entity.Message;
import info.saladlam.example.spring.noticeboard.repository.MessageRepository;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	private Mapper mapper;
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public MessageDto findOne(long id, LocalDateTime time) {
		Message from = this.messageRepository.findOne(id);
		MessageDto to = mapper.map(from, MessageDto.class);
		this.updateStatus(to, time);
		return to;
	}

	@Override
	public List<MessageDto> findPublished(LocalDateTime time) {
		List<Message> publishedMessage = this.messageRepository.findPublished(time);
		return publishedMessage.stream().map(from -> {
			MessageDto to = mapper.map(from, MessageDto.class);
			to.setStatus(MessageDto.PUBLISHED);
			return to;
		}).collect(Collectors.toList());
	}

	@Override
	public List<MessageDto> findWaitingApprove() {
		List<Message> publishedMessage = this.messageRepository.findWaitingApprove();
		return publishedMessage.stream().map(from -> {
			MessageDto to = mapper.map(from, MessageDto.class);
			to.setStatus(MessageDto.WAITING_APPROVE);
			return to;
		}).collect(Collectors.toList());
	}

	@Override
	public List<MessageDto> findByOwner(String owner, LocalDateTime time) {
		List<Message> publishedMessage = this.messageRepository.findByOwner(owner);
		return publishedMessage.stream().map(from -> {
			MessageDto to = mapper.map(from, MessageDto.class);
			this.updateStatus(to, time);
			return to;
		}).collect(Collectors.toList());
	}

	@Override
	public void save(MessageDto messageDto) {
		if (Objects.nonNull(messageDto.getRemoveDate())
				&& (messageDto.getPublishDate().isAfter(messageDto.getRemoveDate()))) {
			return;
		}

		Message message = mapper.map(messageDto, Message.class);
		this.messageRepository.save(message);
	}

	@Override
	public void approve(long id, String approvedBy, LocalDateTime time) {
		Message message = this.messageRepository.findOne(id);
		if (Objects.isNull(message.getApprovedBy())) {
			message.setApprovedBy(approvedBy);
			message.setApprovedDate(time);
			this.messageRepository.save(message);
		}
	}

	private void updateStatus(MessageDto message, LocalDateTime currentTime) {
		if (Objects.isNull(message.getApprovedBy())) {
			message.setStatus(MessageDto.WAITING_APPROVE);
		} else {
			if (message.getPublishDate().isBefore(currentTime)) {
				message.setStatus(MessageDto.PUBLISHED);
			} else {
				message.setStatus(MessageDto.APPROVED);
			}
			if (Objects.nonNull(message.getRemoveDate()) && (message.getRemoveDate().isBefore(currentTime))) {
				message.setStatus(MessageDto.EXPIRED);
			}
		}
	}

}
