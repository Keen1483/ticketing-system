package com.devskills.ticketingsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.devskills.ticketingsystem.filter.CustomAuthorizationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(STATELESS);
		http.authorizeHttpRequests().requestMatchers(GET, "/users").permitAll();
		http.authorizeHttpRequests().requestMatchers(POST, "/users").permitAll();
		http.authorizeHttpRequests().requestMatchers(PUT, "/users").permitAll();
		http.authorizeHttpRequests().requestMatchers(GET, "/users/[0-9]+/ticket").authenticated();
		http.authorizeHttpRequests().requestMatchers("/tickets/**").permitAll();
		// http.authorizeHttpRequests().requestMatchers(POST, "/roles/**").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeHttpRequests().anyRequest().authenticated();
		http.apply(MyCustomDsl.customDsl());
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
