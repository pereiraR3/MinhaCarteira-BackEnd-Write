package com.minhaCarteira.crud.domain.gasto.DTOs;

public record GastoByFilterDTO(
        Integer usuarioId,
        Integer categoriaId,
        String nome
){ }
