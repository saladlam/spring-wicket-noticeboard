package info.saladlam.example.spring.noticeboard.wicket.component;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.util.Locale;

public class MessageStatusLabel extends Label {

    public MessageStatusLabel(String id) {
        super(id);
    }

    public MessageStatusLabel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected IConverter<?> createConverter(Class<?> type) {
        return new IConverter<Integer>() {
            @Override
            public Integer convertToObject(String value, Locale locale) throws ConversionException {
                throw new RuntimeException("Not to be called!");
            }

            @Override
            public String convertToString(Integer value, Locale locale) {
                if (MessageDto.WAITING_APPROVE.equals(value)) {
                    return getString("status.waitingApprove");
                }
                if (MessageDto.APPROVED.equals(value)) {
                    return getString("status.approved");
                }
                if (MessageDto.PUBLISHED.equals(value)) {
                    return getString("status.published");
                }
                if (MessageDto.EXPIRED.equals(value)) {
                    return getString("status.expired");
                }
                return null;
            }
        };
    }

}
