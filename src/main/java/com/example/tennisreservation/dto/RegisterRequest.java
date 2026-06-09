package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @Schema(description = "Unique username", example = "alice")
        @NotBlank String username,
        @Schema(description = "Raw password", example = "s3cret")
        @NotBlank String password
) {}
