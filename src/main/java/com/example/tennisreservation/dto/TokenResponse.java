package com.example.tennisreservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "JWT access token") String accessToken,
        @Schema(description = "JWT refresh token") String refreshToken,
        @Schema(description = "Token type", example = "Bearer") String tokenType) {

    public static TokenResponse bearer(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken, "Bearer");
    }
}
