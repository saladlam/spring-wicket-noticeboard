package info.saladlam.example.spring.noticeboard.test;

import javax.sql.DataSource;

import info.saladlam.example.spring.noticeboard.entity.Message;
import info.saladlam.example.spring.noticeboard.support.Helper;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import info.saladlam.example.spring.noticeboard.repository.MessageRepository;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MessageRepositoryTest {

	@TestConfiguration
	static class MessageRepositoryTestContextConfiguration {

		@Bean
		public DataSource testDataSource() {
			return Helper.getEmbeddedDatabaseBuilder("noticeboard").build();
		}

		@Bean
		public SpringLiquibase liquibase(DataSource dataSource) {
			SpringLiquibase liquibase = new SpringLiquibase();
			liquibase.setChangeLog("classpath:db/message-repository-test.xml");
			liquibase.setDataSource(dataSource);
			liquibase.setDropFirst(true);
			return liquibase;
		}

	}

	@Autowired
	private MessageRepository messageRepository;

	private Message getM1() {
		Message message = new Message();
		message.setId(1L);
		message.setPublishDate(LocalDateTime.of(2022, 9, 4, 10, 0));
		message.setOwner("user1");
		message.setDescription("Waiting approve message 1");
		return message;
	}

	private Message getM2() {
		Message message = new Message();
		message.setId(2L);
		message.setPublishDate(LocalDateTime.of(2022, 9, 4, 10, 30));
		message.setOwner("user2");
		message.setDescription("Waiting approve message 2");
		return message;
	}

	private Message getM3() {
		Message message = new Message();
		message.setId(3L);
		message.setPublishDate(LocalDateTime.of(2022, 9, 4, 11, 0));
		message.setOwner("user1");
		message.setDescription("Approved message 1");
		message.setApprovedBy("admin");
		message.setApprovedDate(LocalDateTime.of(2022, 9, 1, 12, 0));
		return message;
	}

	private Message getM4() {
		Message message = new Message();
		message.setId(4L);
		message.setPublishDate(LocalDateTime.of(2022, 9, 4, 11, 30));
		message.setOwner("user2");
		message.setDescription("Approved message 2");
		message.setApprovedBy("admin");
		message.setApprovedDate(LocalDateTime.of(2022, 9, 1, 12, 0));
		return message;
	}

	private Message getMNew() {
		Message message = new Message();
		message.setPublishDate(LocalDateTime.of(2022, 9, 4, 12, 0));
		message.setOwner("admin");
		message.setDescription("New message");
		return message;
	}

	@Test
	void findPublished() {
		List<Message> messageList = messageRepository.findPublished(LocalDateTime.of(2022, 9, 4, 11, 30));

		assertEquals(2, messageList.size());
		assertTrue(messageList.contains(getM3()));
		assertTrue(messageList.contains(getM4()));
	}

	@Test
	void findWaitingApprove() {
		List<Message> messageList = messageRepository.findWaitingApprove();

		assertEquals(2, messageList.size());
		assertTrue(messageList.contains(getM1()));
		assertTrue(messageList.contains(getM2()));
	}

	@Test
	void findByOwner() {
		List<Message> messageList = messageRepository.findByOwner("user2");

		assertEquals(2, messageList.size());
		assertTrue(messageList.contains(getM4()));
		assertTrue(messageList.contains(getM2()));
	}

	@Test
	void zTestSave() {
		messageRepository.save(getMNew());
		Message message = messageRepository.findOne(5L);
		Message exceptedMessage = getMNew();
		exceptedMessage.setId(5L);
		assertEquals(exceptedMessage, message);
	}

}
