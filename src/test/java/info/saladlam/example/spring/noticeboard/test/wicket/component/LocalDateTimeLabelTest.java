package info.saladlam.example.spring.noticeboard.test.wicket.component;

import info.saladlam.example.spring.noticeboard.ApplicationConstant;
import info.saladlam.example.spring.noticeboard.wicket.component.LocalDateTimeLabel;
import org.apache.wicket.Session;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeLabelTest {

    private static final String ID = "label";

    private WicketTester tester;

    @BeforeEach
    void setup() {
        tester = new WicketTester();
    }

    @AfterEach
    void tear() {
        tester.destroy();
    }

    @Test
    void testValue() {
        LocalDateTimeLabel label = new LocalDateTimeLabel(ID, Model.of(LocalDateTime.of(2024, 10, 31, 15, 32, 9)));
        tester.startComponentInPage(label);
        assertThat(tester.getTagByWicketId(ID).getValue()).isEqualTo("31-10-2024 15:32");
    }

    @Test
    void testNullLangEn() {
        Session.get().setLocale(new Locale(ApplicationConstant.LOCALE_EN));
        LocalDateTimeLabel label = new LocalDateTimeLabel(ID, Model.of((LocalDateTime) null));
        tester.startComponentInPage(label);
        assertThat(tester.getTagByWicketId(ID).getValue()).isEqualTo("Not set");
    }

    @Test
    void testNullLangJa() {
        Session.get().setLocale(new Locale(ApplicationConstant.LOCALE_JA));
        LocalDateTimeLabel label = new LocalDateTimeLabel(ID, Model.of((LocalDateTime) null));
        tester.startComponentInPage(label);
        assertThat(tester.getTagByWicketId(ID).getValue()).isEqualTo("設定されていない");
    }

}
