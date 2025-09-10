package com.minhaCarteira.write.domain.usuario.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Objeto de Transferência de Dados para login de usuário")
public record UsuarioLoginDTO(

        @NotEmpty
        @Schema(description = "Email do usuário", example = "teste@teste.com")
        String email,

        @NotEmpty
        @Schema(description = "Senha do usuário", example = "*****")
        String senha
) {
}