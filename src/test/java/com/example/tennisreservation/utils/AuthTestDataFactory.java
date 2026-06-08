package com.example.tennisreservation.utils;

import com.example.tennisreservation.security.JwtProperties;
import java.time.Duration;

public final class AuthTestDataFactory {

    public static final String SECRET =
            "test-secret-key-must-be-at-least-256-bits-long-0123456789abcdef";

    private AuthTestDataFactory() {}

    public static JwtProperties jwtProperties() {
        return jwtProperties(Duration.ofMinutes(15), Duration.ofDays(7));
    }

    public static JwtProperties jwtProperties(Duration accessExpiration, Duration refreshExpiration) {
        return new JwtProperties(SECRET, accessExpiration, refreshExpiration);
    }
}
