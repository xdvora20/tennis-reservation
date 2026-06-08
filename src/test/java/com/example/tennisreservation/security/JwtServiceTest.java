package com.example.tennisreservation.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.utils.AuthTestDataFactory;
import com.example.tennisreservation.utils.UserTestDataFactory;
import io.jsonwebtoken.JwtException;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(AuthTestDataFactory.jwtProperties());

    private final User user = UserTestDataFactory.user();

    @Test
    void generateAccessToken_thenExtractUsernameAndRole_roundTrips() {
        String token = jwtService.generateAccessToken(user);

        assertThat(jwtService.extractUsername(token)).isEqualTo(UserTestDataFactory.USERNAME);
        assertThat(jwtService.extractRole(token)).isEqualTo("USER");
    }

    @Test
    void generateAccessToken_isMarkedAccessNotRefresh() {
        String token = jwtService.generateAccessToken(user);

        assertThat(jwtService.isAccessToken(token)).isTrue();
        assertThat(jwtService.isRefreshToken(token)).isFalse();
    }

    @Test
    void generateRefreshToken_isMarkedRefreshNotAccess() {
        String token = jwtService.generateRefreshToken(user);

        assertThat(jwtService.isRefreshToken(token)).isTrue();
        assertThat(jwtService.isAccessToken(token)).isFalse();
    }

    @Test
    void extractUsername_expiredToken_throwsJwtException() {
        JwtService expiringService =
                new JwtService(
                        AuthTestDataFactory.jwtProperties(
                                Duration.ofSeconds(-1), Duration.ofSeconds(-1)));
        String token = expiringService.generateAccessToken(user);

        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(() -> expiringService.extractUsername(token));
    }

    @Test
    void extractUsername_tamperedToken_throwsJwtException() {
        String token = jwtService.generateAccessToken(user);
        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(() -> jwtService.extractUsername(tampered));
    }
}
