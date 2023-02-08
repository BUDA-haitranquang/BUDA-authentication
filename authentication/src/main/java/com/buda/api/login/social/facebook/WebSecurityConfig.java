package com.buda.api.login.social.facebook;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.buda.api.login.social.facebook.oauth.CustomOAuth2User;
import com.buda.api.login.social.facebook.oauth.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private CustomOAuth2UserService oauth2UserService;

	@Autowired
	private UserService userService;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
				.antMatchers("/", "/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().permitAll()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("pass")
				.defaultSuccessUrl("/list")
				.and()
				.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(oauth2UserService)
				.and()
				.successHandler(new AuthenticationSuccessHandler() {

					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
														Authentication authentication) throws IOException, ServletException {
						CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

						userService.processOAuthPostLogin(oauthUser.getEmail());

						response.sendRedirect("/list");
					}
				})
				.and()
				.logout().logoutSuccessUrl("/").permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/403");

		return http.build();
	}
}
