package com.example.tennisreservation.dto;

import com.example.tennisreservation.entity.GameType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateReservationRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull GameType gameType
) {}
