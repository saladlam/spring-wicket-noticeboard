package info.saladlam.example.spring.noticeboard.wicket.model;

import info.saladlam.example.spring.noticeboard.support.Helper;
import org.apache.wicket.model.LoadableDetachableModel;
import org.springframework.security.web.WebAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class HttpSessionRuntimeExceptionModel extends LoadableDetachableModel<RuntimeException> {

    private static final String SESSION_KEY = WebAttributes.AUTHENTICATION_EXCEPTION;

    @Override
    protected RuntimeException load() {
        HttpSession session = Helper.getHttpServletRequestFromRequestCycle().getSession(false);
        if (Objects.nonNull(session)) {
            Object ex = session.getAttribute(SESSION_KEY);
            if (Objects.nonNull(ex) && ex instanceof RuntimeException) {
                return (RuntimeException) ex;
            }
        }
        return null;
    }

}
