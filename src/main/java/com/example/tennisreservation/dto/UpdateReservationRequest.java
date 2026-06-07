package com.example.tennisreservation.dto;

import com.example.tennisreservation.entity.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateReservationRequest(
        @Schema(description = "Reservation start", example = "2030-01-01T10:00:00")
        @NotNull LocalDateTime startTime,
        @Schema(description = "Reservation end", example = "2030-01-01T11:00:00")
        @NotNull LocalDateTime endTime,
        @Schema(description = "Game type", example = "SINGLES")
        @NotNull GameType gameType
) {}
