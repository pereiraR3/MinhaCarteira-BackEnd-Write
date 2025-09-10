package com.minhaCarteira.write.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Funções de usuário no sistema")
public enum UsuarioRole {

    @Schema(description = "Usuario", example = "USER")
    USER,

    @Schema(description = "Administrador", example = "ADMIN")
    ADMIN
}
