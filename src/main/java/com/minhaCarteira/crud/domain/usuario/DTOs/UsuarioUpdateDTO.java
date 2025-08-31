package com.minhaCarteira.crud.domain.usuario.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO usado em PUT de usuário")
public record UsuarioUpdateDTO (

    @Schema(description = "identificador único do usuário", example = "1")
    Integer id,

    @Schema(description = "nome do usuário", example = "teste")
    String nome,

    @Schema(description = "email do usuário", example = "teste@teste.com.br")
    String email
){

}
