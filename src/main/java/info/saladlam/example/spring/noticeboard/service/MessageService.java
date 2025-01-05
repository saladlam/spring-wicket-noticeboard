package info.saladlam.example.spring.noticeboard.service;

import java.time.LocalDateTime;
import java.util.List;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;

public interface MessageService {

	MessageDto findOne(long id, LocalDateTime time);

	List<MessageDto> findPublished(LocalDateTime time);

	List<MessageDto> findWaitingApprove();

	List<MessageDto> findByOwner(String owner, LocalDateTime time);

	void save(MessageDto messageDto);

	void approve(long id, String approvedBy, LocalDateTime time);

}
