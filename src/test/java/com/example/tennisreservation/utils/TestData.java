package com.example.tennisreservation.utils;

import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.entity.GameType;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.entity.SurfaceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TestData {

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
}
