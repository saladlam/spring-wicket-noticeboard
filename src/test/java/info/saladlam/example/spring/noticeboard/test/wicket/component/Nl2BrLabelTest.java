package info.saladlam.example.spring.noticeboard.test.wicket.component;

import info.saladlam.example.spring.noticeboard.wicket.component.Nl2BrLabel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Nl2BrLabelTest {

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
    void testNull() {
        Nl2BrLabel label = new Nl2BrLabel(ID, Model.of((String) null));
        tester.startComponentInPage(label);
        assertThat(tester.getTagByWicketId(ID).getValue()).isEmpty();
    }

    private void buildTest(String input, String expected) {
        Nl2BrLabel label = new Nl2BrLabel(ID, Model.of(input));
        tester.startComponentInPage(label);
        assertThat(tester.getTagByWicketId(ID).getValue()).isEqualTo(expected);
    }

    @Test
    void testSingleLine() {
        buildTest("<b>Hello world!</b>", "&lt;b&gt;Hello world!&lt;/b&gt;");
    }

    @Test
    void testMultipleLines() {
        buildTest("<b>Hello world!</b>\r\nLine 2\nLast line", "&lt;b&gt;Hello world!&lt;/b&gt;<br />\r\nLine 2<br />\nLast line");
    }

    @Test
    void testStartEndEmptyLines() {
        buildTest("\rLine 1\r\n", "\rLine 1<br />\r\n");
    }

}
