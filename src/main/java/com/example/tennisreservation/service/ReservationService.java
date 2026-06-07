package com.example.tennisreservation.service;

import com.example.tennisreservation.dao.ReservationDao;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.exception.BadRequestException;
import com.example.tennisreservation.exception.NotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDao reservationDao;

    public Reservation create(Reservation reservation) {
        validateInterval(reservation.getStartTime(), reservation.getEndTime());
        validateNoOverlap(reservation, null);
        reservation.setTotalPrice(calculatePrice(reservation));
        return reservationDao.save(reservation);
    }

    public Reservation update(Reservation reservation) {
        validateInterval(reservation.getStartTime(), reservation.getEndTime());
        validateNoOverlap(reservation, reservation.getId());
        reservation.setTotalPrice(calculatePrice(reservation));
        return reservationDao.save(reservation);
    }

    public Reservation getById(Long id) {
        return reservationDao
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + id));
    }

    public List<Reservation> getByCourtNumber(Integer courtNumber) {
        return reservationDao.findByCourtNumber(courtNumber);
    }

    public List<Reservation> getByCustomerPhone(String phoneNumber, boolean futureOnly) {
        return reservationDao.findByCustomerPhone(phoneNumber, futureOnly);
    }

    public void delete(Long id) {
        if (!reservationDao.deleteById(id)) {
            throw new NotFoundException("Reservation not found: " + id);
        }
    }

    private void validateInterval(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new BadRequestException("Reservation end time must be after start time");
        }
    }

    private void validateNoOverlap(Reservation reservation, Long excludeReservationId) {
        boolean overlaps =
                reservationDao.existsOverlap(
                        reservation.getCourt().getId(),
                        reservation.getStartTime(),
                        reservation.getEndTime(),
                        excludeReservationId);
        if (overlaps) {
            throw new BadRequestException(
                    "Reservation overlaps an existing reservation on this court");
        }
    }

    private BigDecimal calculatePrice(Reservation reservation) {
        long minutes =
                Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();
        return reservation
                .getCourt()
                .getSurfaceType()
                .getPricePerMinute()
                .multiply(BigDecimal.valueOf(minutes))
                .multiply(reservation.getGameType().getPriceMultiplier())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
