package com.example.tennisreservation.dto;

import com.example.tennisreservation.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "User id", example = "1")
        Long id,
        @Schema(description = "Username", example = "alice")
        String username,
        @Schema(description = "Role", example = "USER")
        Role role
) {}
