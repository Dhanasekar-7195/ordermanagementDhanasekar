package com.ordermanagement.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.ordermanagement.JWTFilter.JWTFilter;
import com.ordermanagement.user.service.GroupUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JWTFilter jwtFilter;

	@Autowired
	private GroupUserDetailsService groupUserDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(groupUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		


		http.csrf().disable().authorizeRequests().antMatchers("/api/user_master/login-authenticate","/api/user_master/logout-success/{id}").permitAll().and()
				.authorizeRequests().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
						"/configuration/security", "/swagger-ui.html", "/webjars/**")
				.permitAll().anyRequest().permitAll();
		
	
				
		http.logout().clearAuthentication(true).deleteCookies("auth_code", "JSESSIONID").invalidateHttpSession(true)
				.logoutSuccessUrl("/api/usermaster/logout-success");

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.invalidSessionUrl("/api/usermaster/session-invalid");

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}
