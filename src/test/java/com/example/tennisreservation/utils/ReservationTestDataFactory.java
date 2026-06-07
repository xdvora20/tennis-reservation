package com.example.tennisreservation.utils;

import com.example.tennisreservation.dto.CreateReservationRequest;
import com.example.tennisreservation.dto.ReservationResponse;
import com.example.tennisreservation.dto.UpdateReservationRequest;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.entity.GameType;
import com.example.tennisreservation.entity.Reservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ReservationTestDataFactory {

    public static final LocalDateTime START = LocalDateTime.of(2030, 1, 1, 10, 0);
    public static final LocalDateTime END = LocalDateTime.of(2030, 1, 1, 11, 0);

    private ReservationTestDataFactory() {}

    public static Reservation reservation() {
        return reservation(
                CourtTestDataFactory.court(),
                CustomerTestDataFactory.customer(),
                START,
                END,
                GameType.SINGLES);
    }

    public static Reservation reservation(
            String pricePerMinute, LocalDateTime start, LocalDateTime end, GameType gameType) {
        Court court =
                CourtTestDataFactory.court(
                        CourtTestDataFactory.COURT_NUMBER,
                        SurfaceTypeTestDataFactory.surfaceType("Clay", pricePerMinute));
        return reservation(court, CustomerTestDataFactory.customer(), start, end, gameType);
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

    public static CreateReservationRequest createReservationRequest() {
        return new CreateReservationRequest(
                CourtTestDataFactory.COURT_NUMBER,
                START,
                END,
                GameType.SINGLES,
                CustomerTestDataFactory.PHONE,
                CustomerTestDataFactory.NAME);
    }

    public static UpdateReservationRequest updateReservationRequest() {
        return new UpdateReservationRequest(START, END, GameType.SINGLES);
    }

    public static ReservationResponse reservationResponse() {
        return new ReservationResponse(
                1L,
                CourtTestDataFactory.COURT_NUMBER,
                CustomerTestDataFactory.NAME,
                CustomerTestDataFactory.PHONE,
                GameType.SINGLES,
                START,
                END,
                new BigDecimal("300.00"),
                null);
    }
}
