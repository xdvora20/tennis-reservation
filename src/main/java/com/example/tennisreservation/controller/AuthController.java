package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.ErrorResponse;
import com.example.tennisreservation.dto.RefreshRequest;
import com.example.tennisreservation.dto.RegisterRequest;
import com.example.tennisreservation.dto.TokenResponse;
import com.example.tennisreservation.dto.UserResponse;
import com.example.tennisreservation.exception.UnauthorizedException;
import com.example.tennisreservation.facade.AuthFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and token issuance")
public class AuthController {

    private static final String BASIC_PREFIX = "Basic ";

    private final AuthFacade authFacade;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new USER account")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Account created",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request or username already taken",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authFacade.register(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Log in with HTTP Basic credentials",
            description =
                    "Send credentials via the Authorization: Basic header. On success the access"
                            + " token is also returned in the Authorization response header.")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Authenticated",
                content = @Content(schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid credentials",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TokenResponse> login(
            @Parameter(hidden = true)
                    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                    String authorization) {
        BasicCredentials credentials = decodeBasic(authorization);
        TokenResponse tokens = authFacade.login(credentials.username(), credentials.password());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.accessToken())
                .body(tokens);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Exchange a refresh token for a new access token")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "New tokens issued",
                content = @Content(schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Missing refresh token",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
                responseCode = "401",
                description = "Invalid or expired refresh token",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        TokenResponse tokens = authFacade.refresh(request.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.accessToken())
                .body(tokens);
    }

    private BasicCredentials decodeBasic(String authorization) {
        if (authorization == null || !authorization.startsWith(BASIC_PREFIX)) {
            throw new UnauthorizedException("Missing Basic authorization header");
        }
        String decoded;
        try {
            decoded = new String(
                    Base64.getDecoder().decode(authorization.substring(BASIC_PREFIX.length())),
                    StandardCharsets.UTF_8
            );
        } catch (IllegalArgumentException _) {
            throw new UnauthorizedException("Malformed Basic authorization header");
        }
        int separator = decoded.indexOf(':');
        if (separator < 0) {
            throw new UnauthorizedException("Malformed Basic authorization header");
        }
        return new BasicCredentials(
                decoded.substring(0, separator), decoded.substring(separator + 1));
    }

    private record BasicCredentials(String username, String password) {}
}
