package info.saladlam.example.spring.noticeboard.support;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

/**
 * Copy from org.springframework.security.test.context.support.WithMockUser and customize.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

	String value() default "user";

	String username() default "";

	String[] roles() default { "USER" };

	String[] authorities() default {};

	String password() default "password";

	String name() default "";

	String email() default "";

	@AliasFor(annotation = WithSecurityContext.class)
	TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;

}
