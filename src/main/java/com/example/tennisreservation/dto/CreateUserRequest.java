package com.example.tennisreservation.dto;

import com.example.tennisreservation.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @Schema(description = "Unique username", example = "alice")
        @NotBlank String username,
        @Schema(description = "Raw password", example = "s3cret")
        @NotBlank String password,
        @Schema(description = "Role granted to the user", example = "USER")
        @NotNull Role role
) {}
