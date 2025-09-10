package com.minhaCarteira.write.domain.usuario.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO usado em POST de usuário")
public record UsuarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Schema(description = "nome do usuário", example = "teste")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        @Schema(description = "email do usuário", example = "teste@teste.com")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        @Schema(description = "senha do usuário", example = "*****")
        String senha
) {}