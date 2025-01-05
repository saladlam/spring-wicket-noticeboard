package info.saladlam.example.spring.noticeboard.config;

import com.giffing.wicket.spring.boot.context.security.AuthenticatedWebSessionConfig;
import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;
import info.saladlam.example.spring.noticeboard.repository.JdbcUserRepository;
import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationWebSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomBeans {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userRepository(JdbcTemplate jdbcTemplate) {
		JdbcUserRepository repository = new JdbcUserRepository();
		repository.setJdbcTemplate(jdbcTemplate);
		return repository;
	}

    @Bean
    public WicketBootSecuredWebApplication wicketBootWebApplication() {
        return new WicketBootSecuredWebApplication();
    }

    @Bean
    public AuthenticatedWebSessionConfig authenticatedWebSessionConfig() {
        return () -> ApplicationWebSession.class;
    }

}
