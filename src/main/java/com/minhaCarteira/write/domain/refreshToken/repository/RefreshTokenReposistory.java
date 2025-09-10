package com.minhaCarteira.write.domain.refreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhaCarteira.write.domain.refreshToken.RefreshToken;

import java.util.Optional;

public interface RefreshTokenReposistory extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
}
