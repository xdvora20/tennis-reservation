package com.example.tennisreservation.dto;

import java.math.BigDecimal;

public record SurfaceTypeResponse(
        Long id,
        String name,
        BigDecimal pricePerMinute
) {}
