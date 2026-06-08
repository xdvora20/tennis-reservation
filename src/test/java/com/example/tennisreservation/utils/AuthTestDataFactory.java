package com.example.tennisreservation.utils;

import com.example.tennisreservation.dto.TokenResponse;
import com.example.tennisreservation.security.JwtProperties;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

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

    public static TokenResponse tokenResponse() {
        return TokenResponse.bearer("access", "refresh");
    }

    public static String basicAuthHeader(String username, String password) {
        return "Basic "
                + Base64.getEncoder()
                        .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
