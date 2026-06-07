package com.example.tennisreservation.utils;

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.dto.CourtResponse;
import com.example.tennisreservation.dto.CreateReservationRequest;
import com.example.tennisreservation.dto.ReservationResponse;
import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.dto.SurfaceTypeResponse;
import com.example.tennisreservation.dto.UpdateReservationRequest;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.entity.GameType;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.entity.SurfaceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TestData {

    public static final LocalDateTime START = LocalDateTime.of(2030, 1, 1, 10, 0);
    public static final LocalDateTime END = LocalDateTime.of(2030, 1, 1, 11, 0);
    public static final String PHONE = "+420700111222";
    public static final String NAME = "Alice";

    private TestData() {}

    public static SurfaceType surfaceType() {
        return surfaceType("Clay", "5.00");
    }

    public static SurfaceType surfaceType(String name, String pricePerMinute) {
        SurfaceType surface = new SurfaceType();
        surface.setName(name);
        surface.setPricePerMinute(new BigDecimal(pricePerMinute));
        return surface;
    }

    public static Court court(int courtNumber, SurfaceType surfaceType) {
        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);
        return court;
    }

    public static Customer customer() {
        return customer("+420700111222", "Alice");
    }

    public static Customer customer(String phoneNumber, String name) {
        Customer customer = new Customer();
        customer.setPhoneNumber(phoneNumber);
        customer.setName(name);
        return customer;
    }

    public static Reservation reservation(
            Court court, Customer customer, LocalDateTime start, LocalDateTime end) {
        return reservation(court, customer, start, end, GameType.SINGLES);
    }

    public static Reservation reservation(
            Court court,
            Customer customer,
            LocalDateTime start,
            LocalDateTime end,
            GameType gameType) {
        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setGameType(gameType);
        reservation.setTotalPrice(BigDecimal.ZERO);
        return reservation;
    }

    public static SurfaceTypeRequest surfaceTypeRequest() {
        return new SurfaceTypeRequest("Clay", new BigDecimal("5.00"));
    }

    public static SurfaceTypeResponse surfaceTypeResponse() {
        return new SurfaceTypeResponse(1L, "Clay", new BigDecimal("5.00"));
    }

    public static CourtRequest courtRequest() {
        return new CourtRequest(1, 1L);
    }

    public static CourtResponse courtResponse() {
        return new CourtResponse(1L, 1, surfaceTypeResponse());
    }

    public static CreateReservationRequest createReservationRequest() {
        return new CreateReservationRequest(1, START, END, GameType.SINGLES, PHONE, NAME);
    }

    public static UpdateReservationRequest updateReservationRequest() {
        return new UpdateReservationRequest(START, END, GameType.SINGLES);
    }

    public static ReservationResponse reservationResponse() {
        return new ReservationResponse(
                1L, 1, NAME, PHONE, GameType.SINGLES, START, END, new BigDecimal("300.00"), null);
    }
}

