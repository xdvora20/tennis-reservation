package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourtRequest(
        @Schema(description = "Unique court number", example = "1")
        @NotNull @Positive Integer courtNumber,
        @Schema(description = "Surface type id", example = "1")
        @NotNull Long surfaceTypeId
) {}
