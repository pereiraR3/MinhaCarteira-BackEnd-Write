package com.minhaCarteira.crud.infra.security.controller;

import com.minhaCarteira.crud.domain.refreshToken.DTOs.RefreshTokenRequestDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioLoginDTO;
import com.minhaCarteira.crud.infra.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Autenticação de Usuário",
            description = "Autentica um usuário com o email + senha, retornando um token de acesso e um refresh token"
    )
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String,String>> authenticate(@Valid @RequestBody UsuarioLoginDTO usuarioLoginDTO)
    {
        logger.info("Tentativa de autenticação para usuário: {}", usuarioLoginDTO.email());
        try {
            Map<String, String> tokens = authenticationService.authenticate(usuarioLoginDTO);
            logger.info("Autenticação bem-sucedida para usuário: {}", usuarioLoginDTO.email());
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            logger.error("Falha na autenticação para usuário: {}. Erro: {}", usuarioLoginDTO.email(), e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Renovar token de acesso",
            description = "Gera um novo token de acesso com base em um refresh token válido."
    )
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO)
    {
        logger.info("Solicitação de refresh token recebida");

        try {
            String newAccessToken = authenticationService.refresh(refreshTokenRequestDTO.refreshToken());
            logger.info("Novo access token gerado com sucesso.");
            return ResponseEntity.ok(newAccessToken);
        } catch (Exception e) {
            logger.error("Erro ao renovar access token: {}", e.getMessage());
            throw e;
        }
    }

}
