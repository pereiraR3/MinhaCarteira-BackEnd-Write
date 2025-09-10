package com.minhaCarteira.write.domain.refreshToken.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO usado para renovar token de acesso")
public record RefreshTokenRequestDTO(

        @Schema(description = "Refresh token emitido", example = "d442323232-dsdasdasda22-2323sdsd23")
        @NotBlank
        String refreshToken

) {
}
