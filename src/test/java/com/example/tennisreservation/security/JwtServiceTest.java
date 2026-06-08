package com.example.tennisreservation.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.utils.AuthTestDataFactory;
import com.example.tennisreservation.utils.UserTestDataFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(AuthTestDataFactory.jwtProperties());

    private final User user = UserTestDataFactory.user();

    @Test
    void accessToken_parsesToUsernameRoleAndAccessType() {
        Claims claims = jwtService.parseClaims(jwtService.generateAccessToken(user));

        assertThat(claims.getSubject()).isEqualTo(UserTestDataFactory.USERNAME);
        assertThat(jwtService.getRole(claims)).isEqualTo("USER");
        assertThat(jwtService.isAccessToken(claims)).isTrue();
    }

    @Test
    void refreshToken_isNotAccessTypeButIsRefreshType() {
        String token = jwtService.generateRefreshToken(user);

        assertThat(jwtService.isAccessToken(jwtService.parseClaims(token))).isFalse();
        assertThat(jwtService.isRefreshToken(token)).isTrue();
    }

    @Test
    void extractUsername_returnsSubject() {
        assertThat(jwtService.extractUsername(jwtService.generateAccessToken(user)))
                .isEqualTo(UserTestDataFactory.USERNAME);
    }

    @Test
    void parseClaims_expiredToken_throwsJwtException() {
        JwtService expiringService =
                new JwtService(
                        AuthTestDataFactory.jwtProperties(
                                Duration.ofSeconds(-1), Duration.ofSeconds(-1)));
        String token = expiringService.generateAccessToken(user);

        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(() -> expiringService.parseClaims(token));
    }

    @Test
    void parseClaims_tamperedToken_throwsJwtException() {
        String token = jwtService.generateAccessToken(user);
        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertThatExceptionOfType(JwtException.class)
                .isThrownBy(() -> jwtService.parseClaims(tampered));
    }
}
