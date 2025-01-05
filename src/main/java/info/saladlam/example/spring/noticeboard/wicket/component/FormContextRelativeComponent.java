package info.saladlam.example.spring.noticeboard.wicket.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.cycle.RequestCycle;

public class FormContextRelativeComponent extends WebMarkupContainer {

    public FormContextRelativeComponent(String id, String link) {
        super(id, Model.of(link));
    }

    public FormContextRelativeComponent(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new AttributeAppender(
                "action",
                renderUrl())
        );
    }

    // copy from org.apache.wicket.markup.html.link.ExternalLink.renderUrl
    private String renderUrl() {
        Object hrefValue = getDefaultModelObject();
        if (hrefValue == null) {
            return null;
        }

        String url = hrefValue.toString();
        if (!url.isEmpty() && url.charAt(0) == '/') {
            url = url.substring(1);
        }
        url = UrlUtils.rewriteToContextRelative(url, RequestCycle.get());
        return url;
    }

}
