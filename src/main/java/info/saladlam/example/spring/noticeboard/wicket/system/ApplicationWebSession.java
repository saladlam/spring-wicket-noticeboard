package info.saladlam.example.spring.noticeboard.wicket.system;

import info.saladlam.example.spring.noticeboard.ApplicationConstant;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;

public class ApplicationWebSession extends AuthenticatedWebSession {

    private static final long serialVersionUID = 1L;

    public ApplicationWebSession(Request request) {
        super(request);

        // set default language
        String language = request.getLocale().getLanguage();
        if (!ApplicationConstant.LOCALE_EN.equals(language) && !ApplicationConstant.LOCALE_JA.equals(language)) {
            setLocale(new Locale(ApplicationConstant.LOCALE_EN));
        }
        // make this not-temporary
        bind();
    }

    public static ApplicationWebSession get() {
        return (ApplicationWebSession) Session.get();
    }

    @Override
    protected boolean authenticate(String username, String password) {
        return true;
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        if (isSignedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                roles.add(authority.getAuthority());
            }
        }
        return roles;
    }

}
