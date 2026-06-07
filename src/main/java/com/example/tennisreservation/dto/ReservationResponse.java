package com.example.tennisreservation.dto;

import com.example.tennisreservation.entity.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record ReservationResponse(
        @Schema(description = "Identifier", example = "1")
        Long id,
        @Schema(description = "Court number", example = "1")
        Integer courtNumber,
        @Schema(description = "Customer name", example = "Alice")
        String customerName,
        @Schema(description = "Customer phone number", example = "+420700111222")
        String phoneNumber,
        @Schema(description = "Game type", example = "SINGLES")
        GameType gameType,
        @Schema(description = "Reservation start", example = "2030-01-01T10:00:00")
        LocalDateTime startTime,
        @Schema(description = "Reservation end", example = "2030-01-01T11:00:00")
        LocalDateTime endTime,
        @Schema(description = "Total price", example = "300.00")
        BigDecimal totalPrice,
        @Schema(description = "Creation timestamp", example = "2026-01-01T12:00:00Z")
        Instant createdAt
) {}
