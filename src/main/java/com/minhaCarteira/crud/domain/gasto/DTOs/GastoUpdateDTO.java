package com.minhaCarteira.crud.domain.gasto.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "DTO usado para atualização de gasto")
public record GastoUpdateDTO(
        Integer id,
        Float valor,
        @Size(max = 200)
        String descricao,
        @Size(max = 50)
        String nome,
        LocalDateTime data,
        Integer categoriaId
){ }
