package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.ErrorResponse;
import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.dto.SurfaceTypeResponse;
import com.example.tennisreservation.facade.SurfaceTypeFacade;
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
@RequestMapping("/api/surface-types")
@RequiredArgsConstructor
@Tag(name = "Surface types", description = "Manage court surface types and their per-minute prices")
public class SurfaceTypeController {

    private final SurfaceTypeFacade surfaceTypeFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a surface type")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Surface type created",
                content = @Content(schema = @Schema(implementation = SurfaceTypeResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SurfaceTypeResponse create(@Valid @RequestBody SurfaceTypeRequest request) {
        return surfaceTypeFacade.create(request);
    }

    @GetMapping
    @Operation(summary = "List all surface types")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "List of surface types",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SurfaceTypeResponse.class)))))
    public List<SurfaceTypeResponse> getAll() {
        return surfaceTypeFacade.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a surface type by id")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Surface type",
                content = @Content(schema = @Schema(implementation = SurfaceTypeResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Surface type not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SurfaceTypeResponse getById(@PathVariable Long id) {
        return surfaceTypeFacade.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a surface type")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Surface type updated",
                content = @Content(schema = @Schema(implementation = SurfaceTypeResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Surface type not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SurfaceTypeResponse update(
            @PathVariable Long id, @Valid @RequestBody SurfaceTypeRequest request) {
        return surfaceTypeFacade.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a surface type")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Surface type deleted"),
        @ApiResponse(
                responseCode = "404",
                description = "Surface type not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void delete(@PathVariable Long id) {
        surfaceTypeFacade.delete(id);
    }
}
