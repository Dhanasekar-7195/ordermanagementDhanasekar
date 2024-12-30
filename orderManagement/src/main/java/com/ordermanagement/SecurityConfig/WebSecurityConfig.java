package com.ordermanagement.SecurityConfig;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ordermanagement.JWTFilter.JWTFilter;
import com.ordermanagement.tenant.TenantContextFilter;
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
        auth.userDetailsService(groupUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers(
                "/api/public/user_master/login-authenticate",
                "/api/public/user_master/add-usermaster",
                "/api/public/user_master/add_password/{email}/{password}",
                "/api/public/email/forget_password",
                "/api/public/email/change_password",
                "/api/public/email/validate_otp",
                "/api/public/user_master/logout-success/**",
                "/api/v1_aws_s3_bucket/view/{fileName}",
                "/api/v1_aws_s3_bucket/upload",
                "/api/public/company/add"
            ).permitAll()
            .antMatchers(
                "/v2/api-docs", 
                "/configuration/ui",
                "/swagger-resources/**", 
                "/configuration/security", 
                "/swagger-ui.html", 
                "/webjars/**"
            ).permitAll()
           .anyRequest().authenticated(); // Only authenticated requests beyond this point
     // .anyRequest().permitAll();

        http.logout()
            .clearAuthentication(true)
            .deleteCookies("auth_code", "JSESSIONID")
            .invalidateHttpSession(true)
            .logoutUrl("/api/user_master/logout-success") // Ensure consistent logout URL
            .logoutSuccessHandler((request, response, authentication) -> {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Logout Successful");
            });

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .invalidSessionUrl("/api/user_master/session-invalid"); // Refined URL

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new TenantContextFilter(), UsernamePasswordAuthenticationFilter.class);

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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
       // configuration.setAllowedOrigins(List.of("https://trusted-frontend.com")); // Replace with the actual frontend domain
        configuration.setAllowedOriginPatterns(List.of("*")); // Replace with the actual frontend domain
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
