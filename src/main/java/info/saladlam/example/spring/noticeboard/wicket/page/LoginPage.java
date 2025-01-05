package info.saladlam.example.spring.noticeboard.wicket.page;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import info.saladlam.example.spring.noticeboard.wicket.component.CsrfComponent;
import info.saladlam.example.spring.noticeboard.wicket.component.FormContextRelativeComponent;
import info.saladlam.example.spring.noticeboard.wicket.model.SpringSecurityCsrfTokenModel;
import info.saladlam.example.spring.noticeboard.wicket.model.HttpSessionRuntimeExceptionModel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.Objects;

// stateless page
@WicketSignInPage
public class LoginPage extends WebPage {

    private final SpringSecurityCsrfTokenModel csrfModel = new SpringSecurityCsrfTokenModel();
    private final HttpSessionRuntimeExceptionModel exceptionModel = new HttpSessionRuntimeExceptionModel();

    public LoginPage() {
        super();
    }

    public LoginPage(final PageParameters parameters) {
        super(parameters);
    }

    private IModel<String> getPageTitle() {
        return new IModel<String>() {
            @Override
            public String getObject() {
                return String.format("%s - %s", getString("applicationName"), getString("login"));
            }
        };
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        add(new Label("pageTitle", getPageTitle()));

        WebMarkupContainer fLogin = new FormContextRelativeComponent("fLogin", "/loginHandler");
        add(fLogin);

        WebMarkupContainer cError = new WebMarkupContainer("cError") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(getPage().getPageParameters().get("error").toBoolean(false) && Objects.nonNull(exceptionModel.getObject()));
            }
        };
        cError.add(new Label("errorMessage", new PropertyModel<>(exceptionModel, "message").map(t -> String.format("%s: %s", getString("reason"), t))));
        fLogin.add(cError);

        fLogin.add(new CsrfComponent("formCsrf", csrfModel));

        setStatelessHint(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(LoginPage.class, "login-page.css")));
    }

    @Override
    public void onDetach() {
        csrfModel.detach();
        exceptionModel.detach();
        super.onDetach();
    }

}
