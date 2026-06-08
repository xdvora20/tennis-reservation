package com.example.tennisreservation.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dto.TokenResponse;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.exception.UnauthorizedException;
import com.example.tennisreservation.security.JwtService;
import com.example.tennisreservation.service.UserService;
import com.example.tennisreservation.utils.UserTestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthFacade authFacade;

    private final User user = UserTestDataFactory.user();

    @Test
    void login_validCredentials_returnsTokens() {
        when(userService.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("s3cret", user.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("access");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh");

        TokenResponse response = authFacade.login("alice", "s3cret");

        assertThat(response.accessToken()).isEqualTo("access");
        assertThat(response.refreshToken()).isEqualTo("refresh");
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void login_wrongPassword_throwsUnauthorized() {
        when(userService.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> authFacade.login("alice", "wrong"));
    }

    @Test
    void login_unknownUser_throwsUnauthorized() {
        when(userService.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> authFacade.login("ghost", "s3cret"));
    }
}
