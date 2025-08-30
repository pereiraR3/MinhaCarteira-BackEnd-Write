package com.minhaCarteira.crud.domain.usuario.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO usado para filtrar listagem de usuários")
public record UsuarioByFilterDTO (

        @Schema(description = "identificador único de usuário")
        Integer id,

        @Schema(description = "nome de usuário")
        String nome,

        @Schema(description = "email único de usuário")
        String email

){

}
