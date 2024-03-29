// package com.buda.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;


// @Configuration
// @EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = false, jsr250Enabled = false)

// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//     @Bean
//   public PasswordEncoder encoder() {
//     return new BCryptPasswordEncoder();
//   }

//   @Override
//   protected void configure(HttpSecurity http) throws Exception {
//     http.authorizeRequests()
//       .antMatchers("/h2-console/**").permitAll().and().csrf().ignoringAntMatchers("/h2-console/**")
//       .and().headers().frameOptions().sameOrigin().and().formLogin();
//   }
// }
