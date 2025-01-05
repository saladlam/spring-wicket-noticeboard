package info.saladlam.example.spring.noticeboard.wicket.component;

import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationLocalDateTimeConverter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class LocalDateTimeLabel extends Label {

    public LocalDateTimeLabel(String id) {
        super(id);
    }

    public LocalDateTimeLabel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected IConverter<?> createConverter(Class<?> type) {
        return ApplicationLocalDateTimeConverter.INSTANCE;
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        String text = getDefaultModelObjectAsString();
        if (text.isEmpty()) {
            text = getString("notSet");
        }
        replaceComponentTagBody(markupStream, openTag, text);
    }

}
