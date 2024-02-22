package com.study.springboot.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.study.springboot.config.jwt.TokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = parseBearerToken(request);

			if (token != null && !token.equalsIgnoreCase("null")) {
				String userId = tokenProvider.validateAndGetUserId(token);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userId, // 인증된 사용자의 정보
						null,    // 암호(패스워드)를 넣는데, JWT에서는 패스워드가 아니므로 null
						AuthorityUtils.NO_AUTHORITIES
						);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				securityContext.setAuthentication(authentication);
				SecurityContextHolder.setContext(securityContext);
			}else {
				log.info("토큰없음");
			}
		} catch (ExpiredJwtException ex) {
			// 토큰 만료 예외 처리
			logger.error("Token has expired. 토큰만료", ex);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
			response.getWriter().write("Token has expired. 토큰만료");
			return;
		} catch (Exception e) {
			logger.error("Could not set user authentication in security context", e);
		}
		filterChain.doFilter(request, response);
		
	}

	private String parseBearerToken(HttpServletRequest request) {
		// Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
