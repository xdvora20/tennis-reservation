package com.example.tennisreservation.dto;

import com.example.tennisreservation.entity.GameType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Integer courtNumber,
        String customerName,
        String phoneNumber,
        GameType gameType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal totalPrice,
        Instant createdAt
) {}
