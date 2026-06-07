package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CourtResponse(
        @Schema(description = "Identifier", example = "1")
        Long id,
        @Schema(description = "Unique court number", example = "1")
        Integer courtNumber,
        @Schema(description = "Surface type of the court")
        SurfaceTypeResponse surfaceType
) {}
