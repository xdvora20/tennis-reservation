package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.CreateReservationRequest;
import com.example.tennisreservation.dto.ReservationResponse;
import com.example.tennisreservation.dto.UpdateReservationRequest;
import com.example.tennisreservation.facade.ReservationFacade;
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
    public ReservationResponse create(@Valid @RequestBody CreateReservationRequest request) {
        return reservationFacade.create(request);
    }

    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable Long id) {
        return reservationFacade.getById(id);
    }

    @PutMapping("/{id}")
    public ReservationResponse update(
            @PathVariable Long id, @Valid @RequestBody UpdateReservationRequest request) {
        return reservationFacade.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationFacade.delete(id);
    }

    @GetMapping("/by-court/{courtNumber}")
    public List<ReservationResponse> getByCourtNumber(@PathVariable Integer courtNumber) {
        return reservationFacade.getByCourtNumber(courtNumber);
    }

    @GetMapping("/by-phone/{phoneNumber}")
    public List<ReservationResponse> getByCustomerPhone(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "false") boolean futureOnly) {
        return reservationFacade.getByCustomerPhone(phoneNumber, futureOnly);
    }
}
