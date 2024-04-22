package com.tonigdev.web.nikelao.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(r -> r.anyRequest().permitAll());
		http.formLogin(login -> login.loginPage("/login").permitAll().defaultSuccessUrl("/", true));
		
		return http.build();
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
