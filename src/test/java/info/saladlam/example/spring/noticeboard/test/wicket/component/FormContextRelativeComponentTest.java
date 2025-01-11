package info.saladlam.example.spring.noticeboard.test.wicket.component;

import info.saladlam.example.spring.noticeboard.wicket.component.FormContextRelativeComponent;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FormContextRelativeComponentTest {

    private static final String ID = "fSubmit";

    private WicketTester tester;

    @BeforeEach
    void setup() {
        tester = new WicketTester() {
            @Override
            protected String createPageMarkup(final String componentId) {
                return "<html><head></head><body><form wicket:id='" + componentId + "' method='post'><button type='submit'>Submit</button></form></body></html>";
            }
        };
    }

    @AfterEach
    void tear() {
        tester.destroy();
    }

    @Test
    void test() {
        FormContextRelativeComponent input = new FormContextRelativeComponent(ID, "/action/submit");
        tester.startComponentInPage(input);
        TagTester tag = tester.getTagByWicketId(ID);
        assertThat(tag.getAttribute("action")).isEqualTo("../action/submit");
    }

}
