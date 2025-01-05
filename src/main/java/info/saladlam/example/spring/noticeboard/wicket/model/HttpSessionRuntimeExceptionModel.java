package info.saladlam.example.spring.noticeboard.wicket.model;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.springframework.security.web.WebAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class HttpSessionRuntimeExceptionModel extends LoadableDetachableModel<RuntimeException> {

    private static final String SESSION_KEY = WebAttributes.AUTHENTICATION_EXCEPTION;

    @Override
    protected RuntimeException load() {
        HttpSession session = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession(false);
        if (Objects.nonNull(session)) {
            Object ex = session.getAttribute(SESSION_KEY);
            if (Objects.nonNull(ex) && ex instanceof RuntimeException) {
                return (RuntimeException) ex;
            }
        }
        return null;
    }

}
