package info.saladlam.example.spring.noticeboard.wicket.page;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.service.ApplicationDateTimeService;
import info.saladlam.example.spring.noticeboard.service.MessageService;
import info.saladlam.example.spring.noticeboard.wicket.component.*;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.*;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;

// stateful page because using Link
@AuthorizeInstantiation({"USER", "ADMIN"})
public class ManagePage extends BasePage {

    @SpringBean
    private MessageService messageService;
    @SpringBean
    private ApplicationDateTimeService timeService;

    public ManagePage() {
        super();
    }

    public ManagePage(final PageParameters parameters) {
        super(parameters);
    }

    private String getLoginName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private LocalDateTime getCurrentLocalDateTime() {
        return timeService.getCurrentLocalDateTime();
    }

    @Override
    protected IModel<String> getPageTitle() {
        return new IModel<>() {
            @Override
            public String getObject() {
                return String.format("%s - %s", getString("applicationName"), getString("manage"));
            }
        };
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        add(new Label("title", getPageTitle()));

        WebMarkupContainer fLogout = new FormContextRelativeComponent("fLogout", "/logout");
        add(fLogout);

        fLogout.add(new CsrfComponent("formCsrf", getCsrfModel()));

        add(new Label("txUser", new StringResourceModel("welcomeUser").setParameters(new PropertyModel<>(getAuthenticationModel(), "principal.name"), new PropertyModel<>(getAuthenticationModel(), "principal.username"))));

        add(new DataView<>("userMessages", new ListDataProvider<MessageDto>(this.messageService.findByOwner(this.getLoginName(), this.getCurrentLocalDateTime())) {
            @Override
            public IModel<MessageDto> model(MessageDto object) {
                return new CompoundPropertyModel<>(object);
            }
        }) {
            @Override
            public void onConfigure() {
                super.onConfigure();
            }

            @Override
            protected void populateItem(Item<MessageDto> item) {
                item.add(new AttributeAppender("id", item.getModel().map(t -> String.format("my%s", t.getId()))));
                item.add(new LocalDateTimeLabel("publishDate").add(new AttributeAppender("id", item.getModel().map(t -> String.format("my%sPublishDate", t.getId())))));
                item.add(new LocalDateTimeLabel("removeDate").add(new AttributeAppender("id", item.getModel().map(t -> String.format("my%sRemovdDate", t.getId())))));
                item.add(new MessageStatusLabel("status").add(new AttributeAppender("id", item.getModel().map(t -> String.format("my%sStatus", t.getId())))));
                item.add(new Nl2BrLabel("description").add(new AttributeAppender("id", item.getModel().map(t -> String.format("my%sDescription", t.getId())))));

                item.add(new Link<>("edit", item.getModel()) {
                    @Override
                    public void onClick() {
                        setResponsePage(new MessagePage(getModel().getObject().getId()));
                    }

                    @Override
                    public void onConfigure() {
                        super.onConfigure();
                        setVisible(MessageDto.WAITING_APPROVE.equals(getModel().getObject().getStatus()));
                    }
                }.add(new AttributeAppender("id", item.getModel().map(t -> String.format("my%sEdit", t.getId())))));
            }
        });

        add(new Link<Void>("buNewMessage") {
            @Override
            public void onClick() {
                setResponsePage(MessagePage.class);
            }
        });

        add(new Label("tWaitingApprove", new ResourceModel("messageWaitingApprove")) {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(getSessionRole().contains("ADMIN"));
            }
        });

        WebMarkupContainer cWaitingApproveMessages = new WebMarkupContainer("cWaitingApproveMessages") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(getSessionRole().contains("ADMIN"));
            }
        };
        cWaitingApproveMessages.add(new DataView<>("waitingApproveMessages", new ListDataProvider<>(this.messageService.findWaitingApprove()) {
            @Override
            public IModel<MessageDto> model(MessageDto object) {
                return new CompoundPropertyModel<>(object);
            }
        }) {
            @Override
            public void onConfigure() {
                super.onConfigure();
            }

            @Override
            protected void populateItem(Item<MessageDto> item) {
                item.add(new AttributeAppender("id", item.getModel().map(t -> String.format("app%s", t.getId()))));
                item.add(new Label("owner").add(new AttributeAppender("id", item.getModel().map(t -> String.format("app%sOwner", t.getId())))));
                item.add(new LocalDateTimeLabel("publishDate").add(new AttributeAppender("id", item.getModel().map(t -> String.format("app%sPublishDate", t.getId())))));
                item.add(new LocalDateTimeLabel("removeDate").add(new AttributeAppender("id", item.getModel().map(t -> String.format("app%sRemoveDate", t.getId())))));
                item.add(new Nl2BrLabel("description").add(new AttributeAppender("id", item.getModel().map(t -> String.format("app%sDescription", t.getId())))));

                item.add(new Link<>("approve", item.getModel()) {
                    @Override
                    public void onClick() {
                        if (getSessionRole().contains("ADMIN")) {
                            messageService.approve(getModel().getObject().getId(), getLoginName(), getCurrentLocalDateTime());
                            setResponsePage(ManagePage.class);
                        } else {
                            throw new AbortWithHttpErrorCodeException(HttpURLConnection.HTTP_FORBIDDEN);
                        }
                    }

                    @Override
                    public void onConfigure() {
                        super.onConfigure();
                        setVisible(MessageDto.WAITING_APPROVE.equals(getModel().getObject().getStatus()));
                    }
                }.add(new AttributeAppender("id", item.getModel().map(t -> String.format("app%sApprove", t.getId())))));
            }
        });
        add(cWaitingApproveMessages);
    }

}
