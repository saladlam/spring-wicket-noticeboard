package info.saladlam.example.spring.noticeboard.dto;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MessageDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MessageDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "publishDate", "publishDate.empty");
		ValidationUtils.rejectIfEmpty(errors, "description", "description.empty");
	}

}
