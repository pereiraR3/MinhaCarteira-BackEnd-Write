package com.minhaCarteira.crud.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Funções de usuário no sistema")
public enum UsuarioRole {

    @Schema(description = "Visitante", example = "VISITANTE")
    VISITANTE,

    @Schema(description = "Administrador", example = "ADMIN")
    ADMIN
}
