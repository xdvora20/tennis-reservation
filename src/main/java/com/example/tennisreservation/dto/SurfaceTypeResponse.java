package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record SurfaceTypeResponse(
        @Schema(description = "Identifier", example = "1")
        Long id,
        @Schema(description = "Surface type name", example = "Clay")
        String name,
        @Schema(description = "Rental price per minute", example = "5.00")
        BigDecimal pricePerMinute
) {}
