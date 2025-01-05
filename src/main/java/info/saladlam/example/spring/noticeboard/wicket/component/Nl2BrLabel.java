package info.saladlam.example.spring.noticeboard.wicket.component;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import java.util.regex.Pattern;

public class Nl2BrLabel extends Label {

    public Nl2BrLabel(String id) {
        super(id);
    }

    public Nl2BrLabel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        setEscapeModelStrings(false);
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        String escape = Strings.escapeMarkup(getDefaultModelObjectAsString(), false, false).toString();
        replaceComponentTagBody(markupStream, openTag, Pattern.compile("(\r\n|\n)").matcher(escape).replaceAll("<br />$1"));
    }

}
