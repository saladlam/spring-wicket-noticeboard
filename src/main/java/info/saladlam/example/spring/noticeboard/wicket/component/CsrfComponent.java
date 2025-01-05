package info.saladlam.example.spring.noticeboard.wicket.component;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.Objects;

public class CsrfComponent extends WebMarkupContainer implements IGenericComponent<CsrfToken, CsrfComponent> {

    public CsrfComponent(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new AttributeAppender(
                "name",
                getModel().map(CsrfToken::getParameterName))
        );
        add(new AttributeAppender(
                "value",
                getModel().map(CsrfToken::getToken))
        );
    }

    @Override
    public void onConfigure() {
        super.onConfigure();
        setVisible(Objects.nonNull(getModelObject()));
    }

}
