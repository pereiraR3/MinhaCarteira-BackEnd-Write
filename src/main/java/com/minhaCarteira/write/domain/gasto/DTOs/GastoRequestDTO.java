package com.minhaCarteira.write.domain.gasto.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "DTO usado para criação de gasto")
public record GastoRequestDTO(

        @NotNull
        @Schema(description = "valor do gasto")
        Float valor,

        @NotBlank
        @Size(max = 200)
        @Schema(description = "descrição do gasto")
        String descricao,

        @NotBlank
        @Size(max = 50)
        @Schema(description = "nome do gasto")
        String nome,

        @NotNull
        @Schema(description = "data do gasto (pode ser outro dia)")
        LocalDateTime data,

        @NotNull
        @Schema(description = "id da categoria")
        Integer categoriaId,

        @NotNull
        @Schema(description = "id do usuário")
        Integer usuarioId

) {}
