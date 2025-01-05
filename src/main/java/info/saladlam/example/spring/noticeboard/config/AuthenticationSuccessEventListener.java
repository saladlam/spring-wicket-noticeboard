package info.saladlam.example.spring.noticeboard.config;

import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationWebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // Authentication token still not in SecurityContextHolder
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes) && (attributes instanceof ServletRequestAttributes)) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
            HttpSession session = servletRequestAttributes.getRequest().getSession(false);
            if (Objects.nonNull(session)) {
                Object s = session.getAttribute("wicket:wicket-filter:session");
                if (Objects.nonNull(s) && (s instanceof ApplicationWebSession)) {
                    LOGGER.debug("wicket Session {} found in HttpSession", s);
                    ((ApplicationWebSession) s).signIn(null, null);
                }
            }
        }
    }

}
