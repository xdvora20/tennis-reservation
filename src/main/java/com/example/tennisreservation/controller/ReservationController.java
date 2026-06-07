package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.CreateReservationRequest;
import com.example.tennisreservation.dto.ErrorResponse;
import com.example.tennisreservation.dto.ReservationResponse;
import com.example.tennisreservation.dto.UpdateReservationRequest;
import com.example.tennisreservation.facade.ReservationFacade;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Create, manage and list court reservations")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a reservation (returns the calculated price)")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Reservation created",
                content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request, bad interval or overlapping reservation",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Court not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReservationResponse create(@Valid @RequestBody CreateReservationRequest request) {
        return reservationFacade.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reservation by id")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservation",
                content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Reservation not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReservationResponse getById(@PathVariable Long id) {
        return reservationFacade.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation (re-prices and re-validates the interval)")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservation updated",
                content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request, bad interval or overlapping reservation",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Reservation not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReservationResponse update(
            @PathVariable Long id, @Valid @RequestBody UpdateReservationRequest request) {
        return reservationFacade.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reservation deleted"),
        @ApiResponse(
                responseCode = "404",
                description = "Reservation not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void delete(@PathVariable Long id) {
        reservationFacade.delete(id);
    }

    @GetMapping("/by-court/{courtNumber}")
    @Operation(summary = "List reservations for a court, ordered by creation time")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "List of reservations",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))))
    public List<ReservationResponse> getByCourtNumber(@PathVariable Integer courtNumber) {
        return reservationFacade.getByCourtNumber(courtNumber);
    }

    @GetMapping("/by-phone/{phoneNumber}")
    @Operation(summary = "List reservations for a phone number, optionally only future ones")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "List of reservations",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))))
    public List<ReservationResponse> getByCustomerPhone(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "false") boolean futureOnly) {
        return reservationFacade.getByCustomerPhone(phoneNumber, futureOnly);
    }
}
