package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record SurfaceTypeRequest(
        @Schema(description = "Surface type name", example = "Clay")
        @NotBlank String name,
        @Schema(description = "Rental price per minute", example = "5.00")
        @NotNull @Positive BigDecimal pricePerMinute
) {}
