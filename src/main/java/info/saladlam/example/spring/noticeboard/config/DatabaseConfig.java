package info.saladlam.example.spring.noticeboard.config;

import javax.sql.DataSource;

import info.saladlam.example.spring.noticeboard.support.Helper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DatabaseConfig {

	@Bean
	@Profile("!test")
	public DataSource dataSource() {
		return Helper.getEmbeddedDatabaseBuilder("noticeboard").build();
	}

}
