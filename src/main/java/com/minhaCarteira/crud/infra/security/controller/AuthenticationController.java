package com.minhaCarteira.crud.infra.security.controller;

import com.minhaCarteira.crud.domain.refreshToken.DTOs.RefreshTokenRequestDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioLoginDTO;
import com.minhaCarteira.crud.infra.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Autenticação de Usuário",
            description = "Autentica um usuário com o siape + senha, retornando um token de acesso e um refresh token"
    )
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String,String>> authenticate(@Valid @RequestBody UsuarioLoginDTO usuarioLoginDTO)
    {
        Map<String, String> tokens = authenticationService.authenticate(usuarioLoginDTO);
        return ResponseEntity.ok(tokens);
    }

    @Operation(
            summary = "Renovar token de acesso",
            description = "Gera um novo token de acesso com base em um refresh token válido."
    )
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO)
    {
        String newAccessToken = authenticationService.refresh(refreshTokenRequestDTO.refreshToken());
        return ResponseEntity.ok(newAccessToken);
    }

}
