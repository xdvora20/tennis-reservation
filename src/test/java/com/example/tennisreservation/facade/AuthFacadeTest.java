package com.example.tennisreservation.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthFacade authFacade;

    private final User user = UserTestDataFactory.user();

    @Test
    void login_validCredentials_returnsTokens() {
        when(userService.findByUsername("alice")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("access");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh");

        TokenResponse response = authFacade.login("alice", "s3cret");

        assertThat(response.accessToken()).isEqualTo("access");
        assertThat(response.refreshToken()).isEqualTo("refresh");
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void login_badCredentials_throwsUnauthorized() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad"));

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> authFacade.login("alice", "wrong"));
    }

    @Test
    void login_userMissingAfterAuthentication_throwsUnauthorized() {
        when(userService.findByUsername("alice")).thenReturn(Optional.empty());

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> authFacade.login("alice", "s3cret"));
    }
}
