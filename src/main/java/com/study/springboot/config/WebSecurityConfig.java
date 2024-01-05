package com.study.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.study.springboot.security.JwtAuthenticationFilter;
import com.study.springboot.service.MemberDetailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	private final MemberDetailService memberDetailService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
				.requestMatchers("/static/**");
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			.cors().and()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.httpBasic().disable()
			.authorizeRequests()
				.requestMatchers("/login","/signup").permitAll()
				.requestMatchers("/mypage/").hasRole("USER")
				.requestMatchers("/admin").hasRole("ADMIN")
				.anyRequest().permitAll()
//			.and()
//			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.and()
			.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter를 AnotherFilter 뒤에 추가
		
		return http.build();
	}

}
