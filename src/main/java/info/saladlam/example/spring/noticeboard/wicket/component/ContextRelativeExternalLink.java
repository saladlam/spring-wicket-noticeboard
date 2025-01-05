package info.saladlam.example.spring.noticeboard.wicket.component;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;

public class ContextRelativeExternalLink extends ExternalLink {

    public ContextRelativeExternalLink(String id, String href, String label) {
        super(id, href, label);
        super.setContextRelative(true);
    }

    public ContextRelativeExternalLink(String id, String href) {
        super(id, href);
        super.setContextRelative(true);
    }

    public ContextRelativeExternalLink(String id, IModel<String> href) {
        super(id, href);
        super.setContextRelative(true);
    }

    public ContextRelativeExternalLink(String id, IModel<String> href, IModel<?> label) {
        super(id, href, label);
        super.setContextRelative(true);
    }

    @Override
    public boolean isContextRelative() {
        return true;
    }

    @Override
    public ContextRelativeExternalLink setContextRelative(final boolean contextRelative) {
        throw new RuntimeException("Not to be called!");
    }

}
