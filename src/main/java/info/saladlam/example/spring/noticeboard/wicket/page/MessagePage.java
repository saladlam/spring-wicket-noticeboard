package info.saladlam.example.spring.noticeboard.wicket.page;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.service.ApplicationDateTimeService;
import info.saladlam.example.spring.noticeboard.service.MessageService;
import info.saladlam.example.spring.noticeboard.wicket.component.CsrfComponent;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.Objects;

@AuthorizeInstantiation({"USER"})
public class MessagePage extends BasePage {

    @SpringBean
    private MessageService messageService;
    @SpringBean
    private ApplicationDateTimeService timeService;

    private Long id;
    private LocalDateTime publishDate;
    private LocalDateTime removeDate;
    private String description = "";

    public MessagePage() {
        super();
    }

    public MessagePage(Long id) {
        super();
        this.id = id;
    }

    private boolean isEdit() {
        return Objects.nonNull(id) && (id > 0);
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
                return String.format("%s - %s", getString("applicationName"), isEdit() ? getString("editMessage") : getString("newMessage"));
            }
        };
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        if (isEdit()) {
            MessageDto message = this.messageService.findOne(id, this.getCurrentLocalDateTime());
            if (Objects.nonNull(message) && message.getStatus().equals(MessageDto.WAITING_APPROVE) && message.getOwner().equals(this.getLoginName())) {
                publishDate = message.getPublishDate();
                removeDate = message.getRemoveDate();
                description = message.getDescription();
                dirty();
            } else {
                throw new AbortWithHttpErrorCodeException(HttpURLConnection.HTTP_FORBIDDEN);
            }
        }

        add(new Label("title", new IModel<>() {
            @Override
            public String getObject() {
                return isEdit() ? getString("editMessage") : getString("newMessage");
            }
        }));

        final FormComponent<LocalDateTime> edPublishDate = new TextField<LocalDateTime>("edPublishDate", new PropertyModel<>(this, "publishDate")).setRequired(true);
        final FormComponent<LocalDateTime> edRemoveDate = new TextField<>("edRemoveDate", new PropertyModel<>(this, "removeDate"));
        final FormComponent<String> edDescription = new TextArea<String>("edDescription", new PropertyModel<>(this, "description")).setRequired(true);

        Form<?> editor = new Form<Void>("editor") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                MessageDto message = getMessageDto();
                if (isEdit()) {
                    MessageDto originalMessage = messageService.findOne(id, getCurrentLocalDateTime());
                    if (Objects.nonNull(originalMessage) && (originalMessage.getStatus().equals(MessageDto.WAITING_APPROVE)) && originalMessage.getOwner().equals(getLoginName())) {
                        originalMessage.setPublishDate(message.getPublishDate());
                        originalMessage.setRemoveDate(message.getRemoveDate());
                        originalMessage.setDescription(message.getDescription());
                        messageService.save(originalMessage);
                    } else {
                        throw new AbortWithHttpErrorCodeException(HttpURLConnection.HTTP_FORBIDDEN);
                    }
                } else {
                    message.setOwner(getLoginName());
                    messageService.save(message);
                }
                setResponsePage(ManagePage.class);
            }
        };
        editor.add(edPublishDate);
        editor.add(edRemoveDate);
        editor.add(edDescription);
        editor.add(new CsrfComponent("formCsrf", getCsrfModel()));
        add(editor);
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDateTime getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(LocalDateTime removeDate) {
        this.removeDate = removeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private MessageDto getMessageDto() {
        MessageDto ret = new MessageDto();
        ret.setId(id);
        ret.setPublishDate(publishDate);
        ret.setRemoveDate(removeDate);
        ret.setDescription(description);
        return ret;
    }

}
