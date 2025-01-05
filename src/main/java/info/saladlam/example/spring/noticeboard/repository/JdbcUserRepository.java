package info.saladlam.example.spring.noticeboard.repository;

import info.saladlam.example.spring.noticeboard.entity.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import java.util.Map;

public class JdbcUserRepository extends JdbcDaoImpl implements UserRepository, UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(JdbcUserRepository.class);

	public static final String DEF_USERS_DETAIL_BY_USERNAME_QUERY = "select name, email "
			+ "from users "
			+ "where username = ? "
			+ "limit 1";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return fillUserDetail(super.loadUserByUsername(username));
	}

	protected UserDetails fillUserDetail(UserDetails user) {
		Map<String, Object> map = getJdbcTemplate().queryForMap(DEF_USERS_DETAIL_BY_USERNAME_QUERY, user.getUsername());
		return new CustomUser(user.getUsername(), user.getPassword(), user.getAuthorities(), (String) map.get("name"), (String) map.get("email"));
	}

}
