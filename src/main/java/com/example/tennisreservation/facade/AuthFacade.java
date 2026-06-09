package com.example.tennisreservation.facade;

import com.example.tennisreservation.dto.RegisterRequest;
import com.example.tennisreservation.dto.TokenResponse;
import com.example.tennisreservation.dto.UserResponse;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.exception.UnauthorizedException;
import com.example.tennisreservation.mapper.UserMapper;
import com.example.tennisreservation.security.JwtService;
import com.example.tennisreservation.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        User user = userService.create(request.username(), request.password(), Role.USER);
        return userMapper.toResponse(user);
    }

    public TokenResponse login(String username, String rawPassword) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, rawPassword));
        } catch (AuthenticationException _) {
            throw new UnauthorizedException("Invalid username or password");
        }
        User user =
                userService
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new UnauthorizedException("Invalid username or password"));
        return TokenResponse.bearer(
                jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user));
    }

    public TokenResponse refresh(String refreshToken) {
        String username;
        try {
            if (!jwtService.isRefreshToken(refreshToken)) {
                throw new UnauthorizedException("Not a refresh token");
            }
            username = jwtService.extractUsername(refreshToken);
        } catch (JwtException _) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }
        User user = userService.findByUsername(username)
                        .orElseThrow(() -> new UnauthorizedException("Invalid or expired refresh token"));
        return TokenResponse.bearer(jwtService.generateAccessToken(user), refreshToken);
    }
}
