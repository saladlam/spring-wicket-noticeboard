package info.saladlam.example.spring.noticeboard.wicket.page;

import info.saladlam.example.spring.noticeboard.wicket.model.SpringSecurityCsrfTokenModel;
import info.saladlam.example.spring.noticeboard.wicket.model.SpringSecurityAuthenticationModel;
import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;

public abstract class BasePage extends WebPage {

    private final SpringSecurityAuthenticationModel authenticationModel = new SpringSecurityAuthenticationModel();
    private final SpringSecurityCsrfTokenModel csrfModel = new SpringSecurityCsrfTokenModel();

    protected BasePage() {
        super();
    }

    protected BasePage(final IModel<?> model) {
        super(model);
    }

    protected BasePage(final PageParameters parameters) {
        super(parameters);
    }

    protected IModel<Authentication> getAuthenticationModel() {
        return authenticationModel;
    }

    protected IModel<CsrfToken> getCsrfModel() {
        return csrfModel;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new Label("pageTitle", getPageTitle()));
    }

    protected abstract IModel<String> getPageTitle();

    protected String getSessionLocaleLanguage() {
        return getSession().getLocale().getLanguage();
    }

    protected boolean isSignedIn() {
        return ((AuthenticatedWebSession) getSession()).isSignedIn();
    }

    protected Roles getSessionRole() {
        return ((ApplicationWebSession) getSession()).getRoles();
    }

    @Override
    public void onDetach() {
        authenticationModel.detach();
        csrfModel.detach();
        super.onDetach();
    }

}
