package info.saladlam.example.spring.noticeboard.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

	private final String name;
	private final String email;

	public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
					  Collection<? extends GrantedAuthority> authorities, String name, String email) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.name = name;
		this.email = email;
	}

	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String name, String email) {
		super(username, password, authorities);
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		String parent = super.toString();
		return parent.substring(0, parent.length() - 1) + ", " +
				"Name=" + this.name + ", " +
				"E-mail=" + this.email + "]";
	}

}
