package info.saladlam.example.spring.noticeboard.test.wicket.component;

import info.saladlam.example.spring.noticeboard.test.wicket.support.TestHelper;
import info.saladlam.example.spring.noticeboard.wicket.component.CsrfComponent;
import info.saladlam.example.spring.noticeboard.wicket.model.SpringSecurityCsrfTokenModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.csrf.CsrfToken;

import static org.assertj.core.api.Assertions.assertThat;

class CsrfComponentTest {

    private static final String ID = "formCsrf";

    private WicketTester tester;
    private IModel<CsrfToken> csrfModel;

    @BeforeEach
    void setup() {
        tester = new WicketTester() {
            @Override
            protected String createPageMarkup(final String componentId) {
                return "<html><head></head><body><form><input wicket:id='" + componentId + "' type='hidden'><button type='submit'>Submit</button></form></body></html>";
            }
        };
        csrfModel = new SpringSecurityCsrfTokenModel();
    }

    @AfterEach
    void tear() {
        csrfModel.detach();
        tester.destroy();
    }

    @Test
    void testHasToken() {
        CsrfToken token = TestHelper.prepareCsrfToken();
        CsrfComponent input = new CsrfComponent(ID, csrfModel);
        tester.startComponentInPage(input);
        TagTester tag = tester.getTagByWicketId(ID);
        assertThat(tag.getAttribute("name")).isEqualTo(token.getParameterName());
        assertThat(tag.getAttribute("value")).isEqualTo(token.getToken());
    }

    @Test
    void testNull() {
        CsrfComponent input = new CsrfComponent(ID, csrfModel);
        tester.startComponentInPage(input);
        TagTester tag = tester.getTagByWicketId(ID);
        assertThat(tag).isNull();
    }

}
