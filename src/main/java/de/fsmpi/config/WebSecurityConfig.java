package de.fsmpi.config;

import de.fsmpi.model.user.UserAuthority;
import de.fsmpi.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserServiceImpl userService;
	private final DaoAuthenticationProvider authenticationProvider;

	@Autowired
	public WebSecurityConfig(UserServiceImpl userService,
							 DaoAuthenticationProvider authenticationProvider) {
		this.userService = userService;
		this.authenticationProvider = authenticationProvider;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
		web
				.ignoring()
				.antMatchers("/js/**")
				.antMatchers("/fonts/**")
				.antMatchers("/css/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.headers() //FIXME: use frame options
				.httpStrictTransportSecurity()
				.disable()

				.and()
				.authorizeRequests()
				.antMatchers("/", "/home", "/user/register")
				.permitAll()

				.antMatchers("/options/*")
				.hasAuthority(UserAuthority.EDIT_OPTIONS.getAuthority())

				.antMatchers("/user/edit/")
				.hasAuthority(UserAuthority.MANAGE_USERS.getAuthority())

				.antMatchers("/user/edit/self")
				.authenticated()

				.antMatchers("/print/show/jobs")
				.hasAuthority(UserAuthority.DO_PRINT.getAuthority())

				.antMatchers("/documents/show")
				.hasAnyAuthority(UserAuthority.VIEW_DOCUMENTS.getAuthority())

				.antMatchers("/documents/show/single", "/documents/data")
				.hasAnyAuthority(UserAuthority.VIEW_PDF.getAuthority())

				.antMatchers("/documents/add", "/documents/edit", "/documents/save", "/documents/import")
				.hasAnyAuthority(UserAuthority.VIEW_PDF.getAuthority())

				.anyRequest()
				.authenticated()

				.and()
				.formLogin()
				.loginPage("/user/login")
				.permitAll()

				.and()
				.logout()
				.logoutSuccessUrl("/user/login")
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(this.userService);
		auth.authenticationProvider(this.authenticationProvider);
	}
}