package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        @Schema(description = "When the error occurred", example = "2026-01-01T12:00:00Z")
        Instant timestamp,
        @Schema(description = "HTTP status code", example = "404")
        int status,
        @Schema(description = "HTTP reason phrase", example = "Not Found")
        String error,
        @Schema(description = "Error detail", example = "Court not found: 99")
        String message,
        @Schema(description = "Request path", example = "/api/courts/99")
        String path,
        @Schema(description = "Field validation errors, when applicable")
        Map<String, String> validationErrors
) {}
