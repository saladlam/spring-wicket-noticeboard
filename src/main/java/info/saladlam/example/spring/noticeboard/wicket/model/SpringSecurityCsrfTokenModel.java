package info.saladlam.example.spring.noticeboard.wicket.model;

import info.saladlam.example.spring.noticeboard.support.Helper;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.Objects;

public class SpringSecurityCsrfTokenModel extends LoadableDetachableModel<CsrfToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityCsrfTokenModel.class);

    @Override
    protected CsrfToken load() {
        Object token = Helper.getHttpServletRequestFromRequestCycle().getAttribute(CsrfToken.class.getName());
        if (Objects.nonNull(token) && token instanceof CsrfToken) {
            LOGGER.debug("CsrfToken {} found", ((CsrfToken) token).getToken());
            return (CsrfToken) token;
        }
        return null;
    }

}
