package com.minhaCarteira.crud.domain.categoria.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO usado para buscar por categorias com filtro")
public record CategoriaByFilterDTO(

        @Schema(description = "Identificador único de categoria", example = "1")
        Integer id,

        @Schema(description = "Nome da categoria", example = "Lazer")
        String nome
) {
}
