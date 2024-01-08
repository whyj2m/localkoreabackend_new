package com.study.springboot.redis;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "refreshToken")
@AllArgsConstructor
@Builder
public class LoginTokenDto {
	@Id
	private String id;
	@Indexed
	private String accessToken;
	@TimeToLive
	private Long expiration;
	
	public void updateAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
