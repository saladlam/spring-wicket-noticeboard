package info.saladlam.example.spring.noticeboard.support;

import info.saladlam.example.spring.noticeboard.entity.CustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copy from org.springframework.security.test.context.support.WithMockUserSecurityContextFactory and customize.
 */
final class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser withUser) {
		String username = StringUtils.hasLength(withUser.username()) ? withUser.username() : withUser.value();
		Assert.notNull(username, () -> withUser + " cannot have null username on both username and value properties");
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String authority : withUser.authorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority));
		}
		if (grantedAuthorities.isEmpty()) {
			for (String role : withUser.roles()) {
				Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
		}
		else if (!(withUser.roles().length == 1 && "USER".equals(withUser.roles()[0]))) {
			throw new IllegalStateException("You cannot define roles attribute " + Arrays.asList(withUser.roles())
					+ " with authorities attribute " + Arrays.asList(withUser.authorities()));
		}
		User principal = new CustomUser(username, withUser.password(), true, true, true, true, grantedAuthorities, withUser.name(), withUser.email());
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
				principal.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}

}
