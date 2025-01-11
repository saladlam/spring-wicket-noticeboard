package info.saladlam.example.spring.noticeboard.test.wicket.page;

import info.saladlam.example.spring.noticeboard.test.wicket.support.TestHelper;
import info.saladlam.example.spring.noticeboard.wicket.page.LoginPage;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.csrf.CsrfToken;

import static org.assertj.core.api.Assertions.assertThat;

class LoginPageTest {

    private WicketTester tester;

    @BeforeEach
    void setup() {
        tester = new WicketTester(new MockApplication() {
            @Override
            protected void init() {
                super.init();
                mountPage("/login", LoginPage.class);
            }
        });
    }

    @AfterEach
    void tear() {
        tester.destroy();
    }

    @Test
    void test() {
        CsrfToken token = TestHelper.prepareCsrfToken();
        tester.startPage(LoginPage.class);
        tester.assertRenderedPage(LoginPage.class);
        assertThat(tester.getTagByWicketId("pageTitle").getValue()).isEqualTo("Notice board - Login");
        TagTester form = tester.getTagByWicketId("fLogin");
        assertThat(form.getAttribute("action")).isEqualTo("loginHandler");
        assertThat(form.getChild("name", token.getParameterName()).getAttribute("value")).isEqualTo(token.getToken());
        assertThat(form.getChild("name", "username").getName()).isEqualTo("input");
        assertThat(form.getChild("name", "password").getName()).isEqualTo("input");
        assertThat(form.getChild("button")).isNotNull();
        tester.assertInvisible("fLogin:cError");
    }

    @Test
    void testError() {
        String message = "Bad credentials";
        tester.getRequest().getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new BadCredentialsException(message));
        tester.startPage(LoginPage.class, new PageParameters().add("error", "true"));
        tester.assertRenderedPage(LoginPage.class);
        tester.assertVisible("fLogin:cError");
        tester.assertLabel("fLogin:cError:errorMessage", "Reason: " + message);
    }

}
