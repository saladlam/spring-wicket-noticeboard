package info.saladlam.example.spring.noticeboard.test.wicket.support;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class MessageDtoArgumentMatcher implements ArgumentMatcher<MessageDto> {

    private final MessageDto m;

    public MessageDtoArgumentMatcher(MessageDto m) {
        this.m = m;
    }

    @Override
    public boolean matches(MessageDto t) {
        return Objects.equals(m.getId(), t.getId())
                && Objects.equals(m.getPublishDate(), t.getPublishDate())
                && Objects.equals(m.getRemoveDate(), t.getRemoveDate())
                && Objects.equals(m.getOwner(), t.getOwner())
                && Objects.equals(m.getDescription(), t.getDescription())
                && Objects.equals(m.getApprovedBy(), t.getApprovedBy())
                && Objects.equals(m.getApprovedDate(), t.getApprovedDate())
                && Objects.equals(m.getStatus(), t.getStatus()
        );
    }

}
