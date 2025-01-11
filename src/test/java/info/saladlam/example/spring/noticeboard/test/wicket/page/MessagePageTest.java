package info.saladlam.example.spring.noticeboard.test.wicket.page;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.service.ApplicationDateTimeService;
import info.saladlam.example.spring.noticeboard.service.MessageService;
import info.saladlam.example.spring.noticeboard.support.WithMockCustomUser;
import info.saladlam.example.spring.noticeboard.test.wicket.support.MessageDtoArgumentMatcher;
import info.saladlam.example.spring.noticeboard.test.wicket.support.TestHelper;
import info.saladlam.example.spring.noticeboard.wicket.page.MessagePage;
import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationLocalDateTimeConverter;
import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationWebSession;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class MessagePageTest {

    @Autowired
    private ApplicationContext applicationContext;
    @MockBean
    private MessageService messageService;
    @MockBean
    private ApplicationDateTimeService timeService;
    private WicketTester tester;
    private LocalDateTime currentLocalDateTime;

    @BeforeEach
    void setup() {
        tester = new WicketTester(new MockApplication() {
            @Override
            protected void init() {
                super.init();
                getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));
                ((ConverterLocator) getConverterLocator()).set(LocalDateTime.class, ApplicationLocalDateTimeConverter.INSTANCE);
            }

            @Override
            public Session newSession(Request request, Response response) {
                return new ApplicationWebSession(request);
            }
        });
        currentLocalDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
    }

    @AfterEach
    void tear() {
        tester.destroy();
    }

    private MessageDto buildMessage() {
        MessageDto message = new MessageDto();
        message.setPublishDate(LocalDateTime.of(2000, 1, 1, 3, 15));
        message.setOwner("user1");
        message.setDescription("Approved message 1\r\nLine 2");
        return message;
    }

    @Test
    void newMessageUi() {
        TestHelper.prepareCsrfToken();
        tester.startPage(MessagePage.class);
        tester.assertRenderedPage(MessagePage.class);
        tester.assertLabel("pageTitle", "Notice board - New message");
        tester.assertInvisible("edFeedback");
        assertThat(tester.getTagByWicketId("formCsrf")).isNotNull();
        assertThat(tester.getTagByWicketId("edPublishDate").getAttribute("value")).isEmpty();
        assertThat(tester.getTagByWicketId("edPublishDate").getAttribute("placeholder")).isEqualTo("DD-MM-YYYY HH:MM");
        assertThat(tester.getTagByWicketId("edRemoveDate").getAttribute("value")).isEmpty();
        assertThat(tester.getTagByWicketId("edRemoveDate").getAttribute("placeholder")).isEqualTo("DD-MM-YYYY HH:MM");
        assertThat(tester.getTagByWicketId("edDescription").getValue()).isEmpty();
        assertThat(tester.getTagById("edSave")).isNotNull();
    }

    @Test
    void requiredFields() {
        tester.startPage(MessagePage.class);
        tester.assertRequired("editor:edPublishDate");
        tester.assertNotRequired("editor:edRemoveDate");
        tester.assertRequired("editor:edDescription");
        FormTester formTester = tester.newFormTester("editor");
        formTester.setValue("edPublishDate", "");
        formTester.setValue("edRemoveDate", "");
        formTester.setValue("edDescription", "");
        formTester.submit();
        assertThat(((FormComponent<?>) tester.getComponentFromLastRenderedPage("editor:edPublishDate")).isValid()).isFalse();
        assertThat(((FormComponent<?>) tester.getComponentFromLastRenderedPage("editor:edRemoveDate")).isValid()).isTrue();
        assertThat(((FormComponent<?>) tester.getComponentFromLastRenderedPage("editor:edDescription")).isValid()).isFalse();
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void submitNewMessage() {
        MessageDto excepted = buildMessage();
        excepted.setRemoveDate(LocalDateTime.of(2000, 1, 2, 23, 59));
        tester.startPage(MessagePage.class);
        tester.setFollowRedirects(false);
        FormTester formTester = tester.newFormTester("editor");
        formTester.setValue("edPublishDate", "01-01-2000 03:15");
        formTester.setValue("edRemoveDate", "02-01-2000 23:59");
        formTester.setValue("edDescription", excepted.getDescription());
        formTester.submit();
        tester.assertNoErrorMessage();
        Mockito.verify(messageService).save(ArgumentMatchers.argThat(new MessageDtoArgumentMatcher(excepted)));
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void editMessageUi() {
        MessageDto msg = buildMessage();
        msg.setId(10L);
        msg.setStatus(MessageDto.WAITING_APPROVE);
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findOne(ArgumentMatchers.eq(10L), ArgumentMatchers.any(LocalDateTime.class))).thenReturn(msg);
        tester.startPage(new MessagePage(msg.getId()));
        tester.assertRenderedPage(MessagePage.class);
        tester.assertLabel("pageTitle", "Notice board - Edit message");
        tester.assertInvisible("edFeedback");
        assertThat(tester.getTagByWicketId("edPublishDate").getAttribute("value")).isEqualTo("01-01-2000 03:15");
        assertThat(tester.getTagByWicketId("edRemoveDate").getAttribute("value")).isEmpty();
        assertThat(tester.getTagByWicketId("edDescription").getValue()).isEqualTo(msg.getDescription());
        assertThat(tester.getTagById("edSave")).isNotNull();
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void submitEditMessage() {
        MessageDto excepted = buildMessage();
        excepted.setId(10L);
        excepted.setRemoveDate(LocalDateTime.of(2000, 1, 2, 23, 59));
        excepted.setStatus(MessageDto.WAITING_APPROVE);
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findOne(ArgumentMatchers.eq(10L), ArgumentMatchers.any(LocalDateTime.class))).thenReturn(excepted);
        tester.startPage(new MessagePage(excepted.getId()));
        tester.assertRenderedPage(MessagePage.class);
        tester.assertInvisible("edFeedback");
        FormTester formTester = tester.newFormTester("editor");
        formTester.setValue("edPublishDate", "2020/01/01 03:15");
        formTester.setValue("edRemoveDate", "2020/01/02 23:15");
        formTester.submit();
        tester.setFollowRedirects(false);
        tester.assertVisible("edFeedback");
        assertThat(tester.getTagByWicketId("edPublishDate").getAttribute("value")).isEqualTo("2020/01/01 03:15");
        assertThat(tester.getTagByWicketId("edRemoveDate").getAttribute("value")).isEqualTo("2020/01/02 23:15");
        formTester = tester.newFormTester("editor");
        formTester.setValue("edPublishDate", "01-01-2020 03:15");
        formTester.setValue("edRemoveDate", "01-02-2020 23:15");
        formTester.submit();
        tester.assertNoErrorMessage();
        Mockito.verify(messageService).save(ArgumentMatchers.argThat(new MessageDtoArgumentMatcher(excepted)));
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void editNotFoundMessage() {
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findOne(ArgumentMatchers.eq(10L), ArgumentMatchers.any(LocalDateTime.class))).thenReturn(null);
        tester.setFollowRedirects(false);
        tester.startPage(new MessagePage(10L));
        assertThat(tester.getLastResponse().getStatus()).isEqualTo(403);
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void editOtherStatusMessage() {
        MessageDto msg = buildMessage();
        msg.setId(10L);
        msg.setStatus(MessageDto.PUBLISHED);
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findOne(ArgumentMatchers.eq(10L), ArgumentMatchers.any(LocalDateTime.class))).thenReturn(msg);
        tester.setFollowRedirects(false);
        tester.startPage(new MessagePage(msg.getId()));
        assertThat(tester.getLastResponse().getStatus()).isEqualTo(403);
    }

    @Test
    @WithMockCustomUser(username = "user2", authorities = {"USER"}, name = "First2 Last2")
    void editOtherUserMessage() {
        MessageDto msg = buildMessage();
        msg.setId(10L);
        msg.setStatus(MessageDto.WAITING_APPROVE);
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findOne(ArgumentMatchers.eq(10L), ArgumentMatchers.any(LocalDateTime.class))).thenReturn(msg);
        tester.setFollowRedirects(false);
        tester.startPage(new MessagePage(msg.getId()));
        assertThat(tester.getLastResponse().getStatus()).isEqualTo(403);
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void saveUnsyncMessage() {
        MessageDto first = buildMessage();
        first.setId(10L);
        first.setRemoveDate(LocalDateTime.of(2000, 1, 2, 23, 59));
        first.setStatus(MessageDto.WAITING_APPROVE);
        MessageDto second = buildMessage();
        second.setId(first.getId());
        second.setRemoveDate(first.getRemoveDate());
        second.setStatus(MessageDto.PUBLISHED);
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findOne(ArgumentMatchers.eq(10L), ArgumentMatchers.any(LocalDateTime.class))).thenReturn(first).thenReturn(second);
        tester.startPage(new MessagePage(first.getId()));
        tester.assertRenderedPage(MessagePage.class);
        tester.setFollowRedirects(false);
        FormTester formTester = tester.newFormTester("editor");
        formTester.setValue("edPublishDate", "01-01-2020 03:15");
        formTester.setValue("edRemoveDate", "01-02-2020 23:15");
        formTester.submit();
        assertThat(tester.getLastResponse().getStatus()).isEqualTo(403);
    }

}
