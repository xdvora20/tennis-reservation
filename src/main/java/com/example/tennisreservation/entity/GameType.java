package com.example.tennisreservation.entity;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {
    SINGLES(new BigDecimal("1.0")),
    DOUBLES(new BigDecimal("1.5"));

    private final BigDecimal priceMultiplier;
}
