package com.minhaCarteira.crud.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Funções de usuário no sistema")
public enum UsuarioRole {

    @Schema(description = "Visitante", example = "ROLE_VISITANTE")
    ROLE_VISITANTE,

    @Schema(description = "Administrador", example = "ROLE_ADMIN")
    ROLE_ADMIN
}
