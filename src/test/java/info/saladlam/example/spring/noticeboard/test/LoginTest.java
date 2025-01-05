package info.saladlam.example.spring.noticeboard.test;

import info.saladlam.example.spring.noticeboard.support.Helper;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.wicket.protocol.http.WicketFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LoginTest {

	@TestConfiguration
	public static class LoginTestContextConfiguration {

		@Bean
		public DataSource testDataSource() {
			return Helper.getEmbeddedDatabaseBuilder(LoginTest.class.getName()).build();
		}

		@Bean
		public SpringLiquibase liquibase(DataSource dataSource) {
			SpringLiquibase liquibase = new SpringLiquibase();
			liquibase.setChangeLog("classpath:db/user-test.xml");
			liquibase.setDataSource(dataSource);
			liquibase.setDropFirst(true);
			return liquibase;
		}

		@Bean
		public Boolean finishWicketFilterInit(FilterRegistrationBean<WicketFilter> filter, ServletContext context) throws ServletException {
			MockFilterConfig config = new MockFilterConfig(context, "wicket-filter");
			config.addInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
			WICKET_FILTER = filter.getFilter();
			WICKET_FILTER.init(config);
			return true;
		}

	}

	private static WicketFilter WICKET_FILTER;

	@Autowired
	private MockMvc mockMvc;

	private Document getDocument(RequestBuilder requestBuilder) throws Exception {
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		return Jsoup.parse(result.getResponse().getContentAsString());
	}

	private void sendLogin(MockHttpServletRequestBuilder requestBuilder, String username, String password, ResultMatcher matcher) throws Exception {
		mockMvc.perform(requestBuilder.with(csrf())
				.param("username", username)
				.param("password", password)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(matcher);
	}

	@Test
	void loadLoginPage() throws Exception {
		Document doc = getDocument(get("/login"));
		assertThat(doc.select("input[name=username]")).hasSize(1);
		assertThat(doc.select("input[name=password]")).hasSize(1);
	}

	@Test
	void loginSuccess() throws Exception {
		sendLogin(post("/loginHandler"), "user1", "dIw8#a-$eW", redirectedUrl("/"));
	}

	@Test
	void loginFail() throws Exception {
		sendLogin(post("/loginHandler"), "user1", "password1", redirectedUrl("/login?error=true"));
	}

	@AfterAll
	static void tear() {
		WICKET_FILTER.destroy();
		WICKET_FILTER = null;
	}

}
