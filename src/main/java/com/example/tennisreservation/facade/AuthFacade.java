package com.example.tennisreservation.facade;

import com.example.tennisreservation.dto.TokenResponse;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.exception.UnauthorizedException;
import com.example.tennisreservation.security.JwtService;
import com.example.tennisreservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse login(String username, String rawPassword) {
        User user =
                userService
                        .findByUsername(username)
                        .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                        .orElseThrow(
                                () -> new UnauthorizedException("Invalid username or password"));
        return TokenResponse.bearer(
                jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user));
    }
}
