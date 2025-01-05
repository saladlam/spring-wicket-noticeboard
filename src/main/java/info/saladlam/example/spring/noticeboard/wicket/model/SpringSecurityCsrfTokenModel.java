package info.saladlam.example.spring.noticeboard.wicket.model;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class SpringSecurityCsrfTokenModel extends LoadableDetachableModel<CsrfToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityCsrfTokenModel.class);

    @Override
    protected CsrfToken load() {
        Object token = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getAttribute(CsrfToken.class.getName());
        if (Objects.nonNull(token) && token instanceof CsrfToken) {
            LOGGER.debug("CsrfToken {} found", ((CsrfToken) token).getToken());
            return (CsrfToken) token;
        }
        return null;
    }

}
