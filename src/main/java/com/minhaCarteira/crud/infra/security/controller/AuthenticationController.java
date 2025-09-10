package com.minhaCarteira.crud.infra.security.controller;

import com.minhaCarteira.crud.domain.refreshToken.DTOs.RefreshTokenRequestDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.user.grant-type}")
    private String grantType;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    @Value("${keyclock_url}")
    private String keyClockUrl;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController() {
    }

    @Operation(
            summary = "Autenticação de Usuário",
            description = "Autentica um usuário com o email + senha, retornando um token de acesso e um refresh token"
    )
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@Valid @RequestBody UsuarioLoginDTO usuarioLoginDTO)
    {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("username", usuarioLoginDTO.email());
        formData.add("password", usuarioLoginDTO.senha());
        formData.add("grant_type", grantType);
        formData.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        var result = rt.postForEntity(keyClockUrl, entity, String.class);

        return result;
    }

    @Operation(
            summary = "Renovar token de acesso",
            description = "Gera um novo token de acesso com base em um refresh token válido."
    )
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO)
    {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("grant_type", grantType);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshTokenRequestDTO.refreshToken());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        var result = rt.postForEntity(keyClockUrl, entity, String.class);

        return result;

    }

}
