package com.minhaCarteira.crud.infra.security.service;

import com.minhaCarteira.crud.domain.refreshToken.RefreshToken;
import com.minhaCarteira.crud.domain.refreshToken.repository.RefreshTokenReposistory;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioLoginDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenReposistory refreshTokenReposistory;

    public AuthenticationService(JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenReposistory refreshTokenReposistory) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenReposistory = refreshTokenReposistory;
    }

    public Map<String, String> authenticate(UsuarioLoginDTO usuarioLoginDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioLoginDTO.email(),
                        usuarioLoginDTO.senha())
        );

        String accessToken = jwtService.generateToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setEmail(authentication.getName());
        refreshTokenEntity.setExpiresAt(Instant.now().plusSeconds(7200L));
        refreshTokenReposistory.save(refreshTokenEntity);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;

    }

    public String refresh(String refreshToken){

        RefreshToken token = refreshTokenReposistory.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if(token.getExpiresAt().isBefore(Instant.now())){
            refreshTokenReposistory.delete(token);
            throw new RuntimeException("Refresh token expirado");
        }

        return jwtService.generateRefreshToken(token.getEmail());

    }

}
