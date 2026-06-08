package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.CreateUserRequest;
import com.example.tennisreservation.dto.ErrorResponse;
import com.example.tennisreservation.dto.UserResponse;
import com.example.tennisreservation.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Manage application user accounts (ADMIN only)")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a user")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "User created",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request or username already taken",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return userFacade.create(request);
    }

    @GetMapping
    @Operation(summary = "List all users")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "List of users",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))))
    public List<UserResponse> getAll() {
        return userFacade.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "User",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UserResponse getById(@PathVariable Long id) {
        return userFacade.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted"),
        @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void delete(@PathVariable Long id) {
        userFacade.delete(id);
    }
}
