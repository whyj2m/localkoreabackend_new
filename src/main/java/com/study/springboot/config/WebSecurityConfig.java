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
//import com.study.springboot.service.MyAuthenticationSuccessHandler;
import com.study.springboot.service.OAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final OAuthService oAuthService;
	
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
				.requestMatchers("/", "/oauth/loginInfo").permitAll()
				.requestMatchers("/login","/signup").permitAll()
				.requestMatchers("/mypage/**","/unregister").permitAll()
				.requestMatchers("/board/touristSpot","/board/touristSpotView/**","/board/companyView/**", "/board/company", "/board/notice").permitAll()
				.requestMatchers("/board/boardWrite","/board/edit/**", "/board/delete/**").permitAll()
				.requestMatchers("/local/**","/localFoods/**", "/place/**", "/festival/**").permitAll()
				.requestMatchers("/search/**").permitAll()
//				.requestMatchers("/mypage").hasRole("USER")
//				.requestMatchers("/mypage/**").hasRole("USER")
//				.requestMatchers("/unregister").hasRole("USER")
//				.requestMatchers("/board/boardWrite","/board/edit/**", "/board/delete/**").hasRole("USER")
				.anyRequest().permitAll()
//				.requestMatchers("/oauth2/authorization/google").permitAll()
//			.and()
//				.oauth2Login()
//				.defaultSuccessUrl("/oauth/loginInfo", true) //OAuth2 성공시 redirect
//				.successHandler(new MyAuthenticationSuccessHandler())
//				.userInfoEndpoint()
//				.userService(oAuthService)
//				.and()
			.and()
			.headers().frameOptions().sameOrigin()
			.and()
			.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter를 AnotherFilter 뒤에 추가
		;
//			.and()
//			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		
		return http.build();
	}
}
