package com.study.springboot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.study.springboot.redis.LoginTokenDto;

public interface LoginTokenRepository extends CrudRepository<LoginTokenDto	, String> {
	Optional<LoginTokenDto> findLoginTokenByAccessToken(String accessToken);
}
