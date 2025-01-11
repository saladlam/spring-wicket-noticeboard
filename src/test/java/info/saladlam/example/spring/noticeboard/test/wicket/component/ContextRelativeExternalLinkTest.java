package info.saladlam.example.spring.noticeboard.test.wicket.component;

import info.saladlam.example.spring.noticeboard.wicket.component.ContextRelativeExternalLink;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ContextRelativeExternalLinkTest {

    private static final String ID = "link";

    private WicketTester tester;

    @BeforeEach
    void setup() {
        tester = new WicketTester() {
            @Override
            protected String createPageMarkup(final String componentId) {
                return "<html><head></head><body><a wicket:id='" + componentId + "'>Link</a></body></html>";
            }
        };
    }

    @AfterEach
    void tear() {
        tester.destroy();
    }

    @Test
    void testLink() {
        ContextRelativeExternalLink link = new ContextRelativeExternalLink(ID, "/login");
        tester.startComponentInPage(link);
        TagTester tag = tester.getTagByWicketId(ID);
        assertThat(tag.getAttribute("href")).isEqualTo("../login");
    }

    @Test
    void testClassBehaviour() {
        ContextRelativeExternalLink link = new ContextRelativeExternalLink(ID, "/login");
        assertThat(link.isContextRelative()).isTrue();
        Throwable thrown = catchThrowable(() -> {
            link.setContextRelative(false);
        });
        assertThat(thrown).isInstanceOf(RuntimeException.class);
    }

}
