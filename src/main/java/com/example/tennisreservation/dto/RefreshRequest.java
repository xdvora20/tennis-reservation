package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @Schema(description = "A valid refresh token")
        @NotBlank String refreshToken
) {}
