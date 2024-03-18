package com.devskills.ticketingsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.devskills.ticketingsystem.filter.CustomAuthorizationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Configuration
@EnableWebSecurity
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
	SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
							"/login/**",
							"/roles/**",
							"/tickets/**"
					).permitAll()
					.requestMatchers(GET, "/users/token/refresh/**").permitAll()
					.requestMatchers(GET, "/users").permitAll()
					.requestMatchers(POST, "/users").permitAll()
					.requestMatchers(PUT, "/users").permitAll()
					.requestMatchers(
							"/v3/api-docs",
							"/v3/api-docs/**",
							"/swagger-ressources",
							"/swagger-ressources/**",
							"/configuration/ui",
							"/configuration/security",
							"/swagger-ui/**",
							"/webjars/**",
							"/swagger-ui.html"
					).permitAll()
					.anyRequest().authenticated()
			);
		http.sessionManagement(sess -> sess.sessionCreationPolicy(STATELESS));
		
		//http.authorizeHttpRequests().requestMatchers(POST, "/login/**").permitAll();
		//http.authorizeHttpRequests().requestMatchers(GET, "/users/token/refresh/**").permitAll();
		//http.authorizeHttpRequests().requestMatchers(GET, "/users").permitAll();
		//http.authorizeHttpRequests().requestMatchers(POST, "/users").permitAll();
		//http.authorizeHttpRequests().requestMatchers(PUT, "/users").permitAll();
		//http.authorizeHttpRequests().requestMatchers(GET, "/users/[0-9]+/ticket").authenticated();
		//http.authorizeHttpRequests().requestMatchers("/tickets/**").permitAll();
		// http.authorizeHttpRequests().requestMatchers(POST, "/roles/**").hasAnyAuthority("ROLE_ADMIN");
		// http.authorizeHttpRequests().anyRequest().authenticated();
		http.apply(MyCustomDsl.customDsl());
		http.addFilterBefore(myFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	CustomAuthorizationFilter myFilter() {
	  return new CustomAuthorizationFilter();
	}

}
