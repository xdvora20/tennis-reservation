package com.example.tennisreservation.dto;

import com.example.tennisreservation.entity.GameType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreateReservationRequest(
        @NotNull @Positive Integer courtNumber,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull GameType gameType,
        @NotBlank String phoneNumber,
        @NotBlank String customerName
) {}
