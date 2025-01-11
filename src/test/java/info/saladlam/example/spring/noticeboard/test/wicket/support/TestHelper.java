package info.saladlam.example.spring.noticeboard.test.wicket.support;

import info.saladlam.example.spring.noticeboard.support.Helper;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

public abstract class TestHelper {

    private TestHelper() {
    }

    public static CsrfToken prepareCsrfToken() {
        CsrfToken token = new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "3f339ef4-728a-427a-a5ea-8159bb934f47");
        Helper.getHttpServletRequestFromRequestCycle().setAttribute(
                CsrfToken.class.getName(),
                token
        );
        return new DefaultCsrfToken(token.getHeaderName(), token.getParameterName(), token.getToken());
    }

}
