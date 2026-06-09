package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.dto.CourtResponse;
import com.example.tennisreservation.dto.ErrorResponse;
import com.example.tennisreservation.facade.CourtFacade;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courts")
@RequiredArgsConstructor
@Tag(name = "Courts", description = "Manage tennis courts")
public class CourtController {

    private final CourtFacade courtFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a court")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Court created",
                content = @Content(schema = @Schema(implementation = CourtResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Surface type not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CourtResponse create(@Valid @RequestBody CourtRequest request) {
        return courtFacade.create(request);
    }

    @GetMapping
    @Operation(summary = "List all courts")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "List of courts",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CourtResponse.class)))))
    public List<CourtResponse> getAll() {
        return courtFacade.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a court by id")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Court",
                content = @Content(schema = @Schema(implementation = CourtResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Court not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CourtResponse getById(@PathVariable Long id) {
        return courtFacade.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a court")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Court updated",
                content = @Content(schema = @Schema(implementation = CourtResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Court or surface type not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CourtResponse update(@PathVariable Long id, @Valid @RequestBody CourtRequest request) {
        return courtFacade.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a court (rejected if it has active reservations)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Court deleted"),
        @ApiResponse(
                responseCode = "404",
                description = "Court not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
                responseCode = "409",
                description = "Court has active reservations",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void delete(@PathVariable Long id) {
        courtFacade.delete(id);
    }
}
