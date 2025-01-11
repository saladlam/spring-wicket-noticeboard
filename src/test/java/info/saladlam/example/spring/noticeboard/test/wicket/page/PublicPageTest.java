package info.saladlam.example.spring.noticeboard.test.wicket.page;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.service.ApplicationDateTimeService;
import info.saladlam.example.spring.noticeboard.service.MessageService;
import info.saladlam.example.spring.noticeboard.support.WithMockCustomUser;
import info.saladlam.example.spring.noticeboard.test.wicket.support.TestHelper;
import info.saladlam.example.spring.noticeboard.wicket.page.PublicPage;
import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationWebSession;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class PublicPageTest {

    private static final MessageDto[] MESSAGES;

    @Autowired
    private ApplicationContext applicationContext;
    @MockBean
    private MessageService messageService;
    @MockBean
    private ApplicationDateTimeService timeService;
    private WicketTester tester;
    private LocalDateTime currentLocalDateTime;

    static {
        MessageDto msg1 = new MessageDto();
        msg1.setId(2L);
        msg1.setPublishDate(LocalDateTime.of(2021, 2, 1, 12, 0));
        msg1.setDescription("Test 1");
        MessageDto msg2 = new MessageDto();
        msg2.setId(5L);
        msg2.setPublishDate(LocalDateTime.of(2021, 2, 28, 15, 3));
        msg2.setDescription("Test\n2");
        MessageDto msg3 = new MessageDto();
        msg3.setId(8L);
        msg3.setPublishDate(LocalDateTime.of(2021, 3, 24, 10, 49));
        msg3.setDescription("Test\r\n3");
        MESSAGES = new MessageDto[]{msg1, msg2, msg3};
    }

    private List<MessageDto> buildMessages() {
        return Lists.newArrayList(MESSAGES);
    }

    private List<MessageDto> buildSingleMessage() {
        return List.of(MESSAGES[0]);
    }

    @BeforeEach
    void setup() {
        tester = new WicketTester(new MockApplication() {
            @Override
            protected void init() {
                super.init();
                getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));
            }

            @Override
            public Class<? extends Page> getHomePage() {
                return PublicPage.class;
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

    @Test
    void anonymousAccess() {
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findPublished(any(LocalDateTime.class))).thenReturn(buildSingleMessage());

        tester.executeUrl("/");
        tester.assertRenderedPage(PublicPage.class);
        assertThat(tester.getTagByWicketId("pageTitle").getValue()).isEqualTo("Notice board");
        tester.assertInvisible("cChEn:buChEn");
        assertThat(tester.getTagByWicketId("buChJa").getAttribute("href")).isEqualTo("?lang=ja");
        assertThat(tester.getTagByWicketId("buLogin").getAttribute("href")).isEqualTo("login");
        tester.assertInvisible("cManage:buManage");
        tester.assertInvisible("cLogout");
        assertThat(tester.getTagsByWicketId("messages")).hasSize(1);
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void userAccess() {
        ((ApplicationWebSession) Session.get()).signIn(null, null);
        TestHelper.prepareCsrfToken();
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findPublished(any(LocalDateTime.class))).thenReturn(buildSingleMessage());

        tester.executeUrl("/");
        tester.assertRenderedPage(PublicPage.class);
        tester.assertInvisible("cChEn:buChEn");
        assertThat(tester.getTagByWicketId("buChJa").getAttribute("href")).isEqualTo("?lang=ja");
        tester.assertInvisible("cLogin");
        assertThat(tester.getTagByWicketId("buManage").getAttribute("href")).isEqualTo("manage");
        assertThat(tester.getTagByWicketId("fLogout").getAttribute("action")).isEqualTo("logout");
        tester.assertVisible("cLogout:fLogout:formCsrf");
        assertThat(tester.getTagsByWicketId("messages")).hasSize(1);
    }

    @Test
    void checkMessages() {
        Mockito.when(timeService.getCurrentLocalDateTime()).thenReturn(currentLocalDateTime);
        Mockito.when(messageService.findPublished(any(LocalDateTime.class))).thenReturn(buildMessages());

        tester.executeUrl("/");
        tester.assertRenderedPage(PublicPage.class);
        List<TagTester> list = tester.getTagsByWicketId("messages");
        assertThat(list).hasSize(3);
        int i = 0;
        for (TagTester t : list) {
            int id = (i++ * 3) + 2;
            assertThat(t.getAttribute("id")).isEqualTo("pub" + id);
            TagTester p = t.getChild("wicket:id", "publishDate");
            assertThat(p.getAttribute("class")).isEqualTo("pubPublishDate");
            assertThat(p.getAttribute("id")).isEqualTo("pub" + id + "PublishDate");
            TagTester d = t.getChild("wicket:id", "description");
            assertThat(d.getAttribute("class")).isEqualTo("pubDescription");
            assertThat(d.getAttribute("id")).isEqualTo("pub" + id + "Description");
        }
    }

}
