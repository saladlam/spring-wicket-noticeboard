package info.saladlam.example.spring.noticeboard.wicket.model;

import org.apache.wicket.model.LoadableDetachableModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SpringSecurityAuthenticationModel extends LoadableDetachableModel<Authentication> {

    @Override
    protected Authentication load() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (Objects.nonNull(context)) {
            return context.getAuthentication();
        }
        return null;
    }

}
