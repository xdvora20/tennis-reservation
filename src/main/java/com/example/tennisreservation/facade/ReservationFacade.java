package com.example.tennisreservation.facade;

import com.example.tennisreservation.dto.CreateReservationRequest;
import com.example.tennisreservation.dto.ReservationResponse;
import com.example.tennisreservation.dto.UpdateReservationRequest;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.mapper.ReservationMapper;
import com.example.tennisreservation.service.CourtService;
import com.example.tennisreservation.service.CustomerService;
import com.example.tennisreservation.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReservationFacade {

    private final ReservationService reservationService;
    private final CourtService courtService;
    private final CustomerService customerService;
    private final ReservationMapper reservationMapper;

    public ReservationResponse create(CreateReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setCourt(courtService.getByNumber(request.courtNumber()));
        reservation.setCustomer(
                customerService.getOrCreate(request.phoneNumber(), request.customerName()));
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setGameType(request.gameType());
        return reservationMapper.toResponse(reservationService.create(reservation));
    }

    public ReservationResponse update(Long id, UpdateReservationRequest request) {
        Reservation existing = reservationService.getById(id);
        existing.setStartTime(request.startTime());
        existing.setEndTime(request.endTime());
        existing.setGameType(request.gameType());
        return reservationMapper.toResponse(reservationService.update(existing));
    }

    @Transactional(readOnly = true)
    public ReservationResponse getById(Long id) {
        return reservationMapper.toResponse(reservationService.getById(id));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getByCourtNumber(Integer courtNumber) {
        return reservationMapper.toResponseList(reservationService.getByCourtNumber(courtNumber));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getByCustomerPhone(String phoneNumber, boolean futureOnly) {
        return reservationMapper.toResponseList(
                reservationService.getByCustomerPhone(phoneNumber, futureOnly));
    }

    public void delete(Long id) {
        reservationService.delete(id);
    }
}
