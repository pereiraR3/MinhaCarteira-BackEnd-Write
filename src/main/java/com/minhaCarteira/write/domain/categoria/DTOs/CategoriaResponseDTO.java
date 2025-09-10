package com.minhaCarteira.write.domain.categoria.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO usado para retornar dados de categoria")
public record CategoriaResponseDTO(

        @Schema(description = "Identificador único de categoria")
        Integer id,

        @Schema(description = "Nome da categoria")
        String nome,

        @Schema(description = "Cor da categoria")
        String corIdentificacao

) {
}
