package info.saladlam.example.spring.noticeboard.wicket.page;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import info.saladlam.example.spring.noticeboard.ApplicationConstant;
import info.saladlam.example.spring.noticeboard.wicket.component.*;
import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.service.ApplicationDateTimeService;
import info.saladlam.example.spring.noticeboard.service.MessageService;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDateTime;

// stateless page
@WicketHomePage
public class PublicPage extends BasePage {

    @SpringBean
    private MessageService messageService;
    @SpringBean
    private ApplicationDateTimeService timeService;

    public PublicPage() {
        super();
    }

    public PublicPage(final PageParameters parameters) {
        super(parameters);
    }

    private LocalDateTime getCurrentLocalDateTime() {
        return timeService.getCurrentLocalDateTime();
    }

    @Override
    protected IModel<String> getPageTitle() {
        return new ResourceModel("applicationName");
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        WebMarkupContainer cChEn = new WebMarkupContainer("cChEn") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(!ApplicationConstant.LOCALE_EN.equals(getSessionLocaleLanguage()));
            }
        };
        cChEn.add(new ContextRelativeExternalLink("buChEn", "/?lang=en"));
        add(cChEn);

        WebMarkupContainer cChJa = new WebMarkupContainer("cChJa") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(!ApplicationConstant.LOCALE_JA.equals(getSessionLocaleLanguage()));
            }
        };
        cChJa.add(new ContextRelativeExternalLink("buChJa", "/?lang=ja"));
        add(cChJa);

        WebMarkupContainer cLogin = new WebMarkupContainer("cLogin") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(!isSignedIn());
            }
        };
        cLogin.add(new ContextRelativeExternalLink("buLogin", "/login"));
        add(cLogin);

        WebMarkupContainer cManage = new WebMarkupContainer("cManage") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(isSignedIn());
            }
        };
        cManage.add(new ContextRelativeExternalLink("buManage", "/manage"));
        add(cManage);

        WebMarkupContainer cLogout = new WebMarkupContainer("cLogout") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(isSignedIn());
            }
        };
        add(cLogout);

        WebMarkupContainer fLogout = new FormContextRelativeComponent("fLogout", "/logout");
        cLogout.add(fLogout);

        fLogout.add(new CsrfComponent("formCsrf", getCsrfModel()));

        add(new DataView<>("messages", new ListDataProvider<>(messageService.findPublished(this.getCurrentLocalDateTime())) {
            @Override
            public IModel<MessageDto> model(MessageDto object) {
                return new CompoundPropertyModel<>(object);
            }
        }) {
            @Override
            protected void populateItem(Item<MessageDto> item) {
                item.add(new AttributeAppender(
                        "id",
                        item.getModel().map(t -> String.format("pub%s", t.getId())))
                );
                item.add(new LocalDateTimeLabel("publishDate")
                        .add(new AttributeAppender(
                                "id",
                                item.getModel().map(t -> String.format("pub%sPublishDate", t.getId())))
                        )
                );
                item.add(new Nl2BrLabel("description")
                        .add(new AttributeAppender(
                                "id",
                                item.getModel().map(t -> String.format("pub%sDescription", t.getId())))
                        )
                );
            }
        });

        setStatelessHint(true);
    }

}
