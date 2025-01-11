package info.saladlam.example.spring.noticeboard.test.wicket.page;

import info.saladlam.example.spring.noticeboard.ApplicationConstant;
import info.saladlam.example.spring.noticeboard.support.WithMockCustomUser;
import info.saladlam.example.spring.noticeboard.wicket.page.BasePage;
import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationWebSession;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class BasePageTest {

    public static class BlankPage extends BasePage {
        public static final String PAGE_TITLE = "Page Title";
        public IModel<String> pageTitleModel = new LoadableDetachableModel<>() {
            @Override
            protected String load() {
                return PAGE_TITLE;
            }
        };

        @Override
        protected IModel<String> getPageTitle() {
            return pageTitleModel;
        }

        @Override
        public IModel<Authentication> getAuthenticationModel() {
            return super.getAuthenticationModel();
        }

        @Override
        public IModel<CsrfToken> getCsrfModel() {
            return super.getCsrfModel();
        }

        @Override
        public String getSessionLocaleLanguage() {
            return super.getSessionLocaleLanguage();
        }

        @Override
        public boolean isSignedIn() {
            return super.isSignedIn();
        }

        @Override
        public Roles getSessionRole() {
            return super.getSessionRole();
        }
    }

    private WicketTester tester;

    @BeforeEach
    void setup() {
        tester = new WicketTester(new MockApplication() {
            @Override
            public Session newSession(Request request, Response response) {
                return new ApplicationWebSession(request);
            }
        });
    }

    @AfterEach
    void tear() {
        tester.destroy();
    }

    @Test
    void testDetachModels() {
        tester.startPage(BlankPage.class);
        tester.assertRenderedPage(BlankPage.class);
        BlankPage page = (BlankPage) tester.getLastRenderedPage();
        assertThat(((LoadableDetachableModel<?>) page.getAuthenticationModel()).isAttached()).isFalse();
        assertThat(((LoadableDetachableModel<?>) page.getCsrfModel()).isAttached()).isFalse();
        assertThat(((LoadableDetachableModel<?>) page.pageTitleModel).isAttached()).isFalse();
    }

    @Test
    void testPageTitle() {
        tester.startPage(BlankPage.class);
        tester.assertLabel("pageTitle", BlankPage.PAGE_TITLE);
    }

    @Test
    void testLocaleLanguage() {
        Session.get().setLocale(new Locale(ApplicationConstant.LOCALE_JA));
        tester.startPage(BlankPage.class);
        tester.assertRenderedPage(BlankPage.class);
        TagTester tagTester = TagTester.createTagByName(tester.getLastResponseAsString(), "html");
        assertThat(tagTester.getAttribute("lang")).isEqualTo(ApplicationConstant.LOCALE_JA);
        BlankPage page = (BlankPage) tester.getLastRenderedPage();
        assertThat(page.getSessionLocaleLanguage()).isEqualTo(ApplicationConstant.LOCALE_JA);
        tester.assertLabel("pageTitle", BlankPage.PAGE_TITLE);
    }

    @Test
    void testAnonymous() {
        tester.startPage(BlankPage.class);
        tester.assertRenderedPage(BlankPage.class);
        BlankPage page = (BlankPage) tester.getLastRenderedPage();
        assertThat(page.isSignedIn()).isFalse();
        assertThat(page.getSessionRole()).hasToString("");
    }

    @Test
    @WithMockCustomUser(username = "user1", authorities = {"USER"}, name = "First Last")
    void testUser() {
        ((ApplicationWebSession) Session.get()).signIn(null, null);
        tester.startPage(BlankPage.class);
        tester.assertRenderedPage(BlankPage.class);
        BlankPage page = (BlankPage) tester.getLastRenderedPage();
        assertThat(page.isSignedIn()).isTrue();
        assertThat(page.getSessionRole()).hasToString("USER");
    }

}
