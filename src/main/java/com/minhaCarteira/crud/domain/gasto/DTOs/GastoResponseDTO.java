package com.minhaCarteira.crud.domain.gasto.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO usado para retorno de gasto")
public record GastoResponseDTO(
        Integer id,
        Float valor,
        String descricao,
        String nome,
        LocalDateTime data,
        LocalDateTime dataCriacao,
        Integer categoriaId,
        Integer usuarioId
){ }
