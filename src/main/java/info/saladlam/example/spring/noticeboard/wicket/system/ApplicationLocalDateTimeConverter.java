package info.saladlam.example.spring.noticeboard.wicket.system;

import org.apache.wicket.util.convert.converter.LocalDateTimeConverter;

import java.time.format.DateTimeFormatter;

public class ApplicationLocalDateTimeConverter extends LocalDateTimeConverter {

    public static final ApplicationLocalDateTimeConverter INSTANCE = new ApplicationLocalDateTimeConverter();

    private ApplicationLocalDateTimeConverter() {
    }

    @Override
    protected DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }

}
