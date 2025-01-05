package info.saladlam.example.spring.noticeboard.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import info.saladlam.example.spring.noticeboard.service.MessageService;
import info.saladlam.example.spring.noticeboard.service.MessageServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.entity.Message;
import info.saladlam.example.spring.noticeboard.repository.MessageRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
class MessageServiceImplTest {

	@TestConfiguration
	static class MessageServiceImplTestContextConfiguration {

		@Bean
		public MessageService messageService() {
			return new MessageServiceImpl();
		}

		@Bean
		public Mapper mapper() {
			List<String> mappingFiles = new ArrayList<>();
			mappingFiles.add("dozerJdk8Converters.xml");

			DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
			dozerBeanMapper.setMappingFiles(mappingFiles);
			return dozerBeanMapper;
		}

	}

	@Autowired
	private MessageService messageService;

	@MockBean
	private MessageRepository messageRepository;

	private Message buildPreApproveMessage() {
		Message msg = new Message();
		msg.setId(1L);
		msg.setPublishDate(LocalDateTime.of(2021, 2, 1, 12, 0));
		msg.setRemoveDate(LocalDateTime.of(2021, 2, 28, 12, 0));
		msg.setOwner("staff1");
		msg.setDescription("Test 1");
		return msg;
	}

	private Message buildStaff1Message() {
		Message msg = new Message();
		msg.setId(2L);
		msg.setPublishDate(LocalDateTime.of(2021, 2, 5, 12, 0));
		msg.setOwner("staff1");
		msg.setDescription("Test staff1 message");
		return msg;
	}

	private Message buildApprovedMessage() {
		Message msg = buildPreApproveMessage();
		msg.setApprovedBy("supervisor1");
		msg.setApprovedDate(LocalDateTime.of(2021, 1, 31, 12, 0));
		return msg;
	}

	@Test
	void findOne_published() {
		Mockito.when(messageRepository.findOne(1L)).thenReturn(buildApprovedMessage());
		MessageDto res = messageService.findOne(1L, LocalDateTime.of(2021, 2, 2, 12, 0));

		Mockito.verify(messageRepository).findOne(1L);
		assertThat(res.getId(), is(1L));
		assertThat(res.getStatus(), is(MessageDto.PUBLISHED));
	}

	@Test
	void findOne_waitForApprove() {
		Mockito.when(messageRepository.findOne(1L)).thenReturn(buildPreApproveMessage());
		MessageDto res = messageService.findOne(1L, LocalDateTime.of(2021, 2, 2, 12, 0));

		assertThat(res.getStatus(), is(MessageDto.WAITING_APPROVE));
	}

	@Test
	void findOne_expired() {
		Mockito.when(messageRepository.findOne(1L)).thenReturn(buildApprovedMessage());
		MessageDto res = messageService.findOne(1L, LocalDateTime.of(2021, 2, 28, 12, 1));

		assertThat(res.getStatus(), is(MessageDto.EXPIRED));
	}

	@Test
	void approve() {
		Mockito.when(messageRepository.findOne(1L)).thenReturn(buildPreApproveMessage());

		String approvedBy = "admin";
		LocalDateTime time = LocalDateTime.now();
		messageService.approve(1L, approvedBy, time);

		Message excepted = buildPreApproveMessage();
		excepted.setApprovedBy(approvedBy);
		excepted.setApprovedDate(time);
		Mockito.verify(messageRepository).findOne(1L);
		Mockito.verify(messageRepository).save(excepted);
	}

	@Test
	void findByOwner_past() {
		List<Message> rList = new ArrayList<>();
		rList.add(buildApprovedMessage());
		rList.add(buildStaff1Message());
		Mockito.when(messageRepository.findByOwner("staff1")).thenReturn(rList);

		List<MessageDto> serviceResult = messageService.findByOwner("staff1", LocalDateTime.of(2021, 1, 31, 12, 15));
		assertThat(serviceResult, hasItems(allOf(hasProperty("id", is(1L)), hasProperty("status", is(MessageDto.APPROVED)))));
		assertThat(serviceResult, hasItems(allOf(hasProperty("id", is(2L)), hasProperty("status", is(MessageDto.WAITING_APPROVE)))));
	}

}
