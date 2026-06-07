package com.example.tennisreservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dao.ReservationDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.entity.GameType;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.exception.BadRequestException;
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.utils.TestData;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;
    @InjectMocks
    private ReservationService service;

    private static final LocalDateTime START = LocalDateTime.of(2030, 1, 1, 10, 0);
    private static final LocalDateTime END = LocalDateTime.of(2030, 1, 1, 11, 0);

    @Test
    void create_singlesGame_savesWithPricePerMinuteTimesMinutes() {
        Reservation reservation = reservation("5.00", START, END, GameType.SINGLES);
        when(reservationDao.existsOverlap(any(), any(), any(), isNull())).thenReturn(false);
        when(reservationDao.save(reservation)).thenReturn(reservation);

        Reservation result = service.create(reservation);

        assertThat(result.getTotalPrice()).isEqualByComparingTo("300.00");
        verify(reservationDao).save(reservation);
    }

    @Test
    void create_doublesGame_appliesOnePointFiveMultiplier() {
        Reservation reservation = reservation("5.00", START, END, GameType.DOUBLES);
        when(reservationDao.existsOverlap(any(), any(), any(), isNull())).thenReturn(false);
        when(reservationDao.save(reservation)).thenReturn(reservation);

        Reservation result = service.create(reservation);

        assertThat(result.getTotalPrice()).isEqualByComparingTo("450.00");
    }

    @Test
    void create_fractionalPrice_roundsHalfUpToTwoDecimals() {
        Reservation reservation =
                reservation("3.33", START, START.plusMinutes(7), GameType.DOUBLES);
        when(reservationDao.existsOverlap(any(), any(), any(), isNull())).thenReturn(false);
        when(reservationDao.save(reservation)).thenReturn(reservation);

        Reservation result = service.create(reservation);

        // 3.33 * 7 * 1.5 = 34.965 -> 34.97
        assertThat(result.getTotalPrice()).isEqualByComparingTo("34.97");
    }

    @Test
    void create_endBeforeStart_throwsBadRequest() {
        Reservation reservation = reservation("5.00", END, START, GameType.SINGLES);

        assertThatThrownBy(() -> service.create(reservation))
                .isInstanceOf(BadRequestException.class);
        verify(reservationDao, never()).save(any());
    }

    @Test
    void create_endEqualsStart_throwsBadRequest() {
        Reservation reservation = reservation("5.00", START, START, GameType.SINGLES);

        assertThatThrownBy(() -> service.create(reservation))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void create_overlappingInterval_throwsBadRequest() {
        Reservation reservation = reservation("5.00", START, END, GameType.SINGLES);
        when(reservationDao.existsOverlap(any(), any(), any(), isNull())).thenReturn(true);

        assertThatThrownBy(() -> service.create(reservation))
                .isInstanceOf(BadRequestException.class);
        verify(reservationDao, never()).save(any());
    }

    @Test
    void update_validReservation_recomputesPriceExcludingItselfFromOverlap() {
        Reservation reservation = reservation("5.00", START, END, GameType.DOUBLES);
        ReflectionTestUtils.setField(reservation, "id", 5L);
        when(reservationDao.existsOverlap(any(), any(), any(), eq(5L))).thenReturn(false);
        when(reservationDao.save(reservation)).thenReturn(reservation);

        Reservation result = service.update(reservation);

        assertThat(result.getTotalPrice()).isEqualByComparingTo("450.00");
        verify(reservationDao).existsOverlap(any(), any(), any(), eq(5L));
    }

    @Test
    void update_overlappingInterval_throwsBadRequest() {
        Reservation reservation = reservation("5.00", START, END, GameType.SINGLES);
        ReflectionTestUtils.setField(reservation, "id", 5L);
        when(reservationDao.existsOverlap(any(), any(), any(), eq(5L))).thenReturn(true);

        assertThatThrownBy(() -> service.update(reservation))
                .isInstanceOf(BadRequestException.class);
        verify(reservationDao, never()).save(any());
    }

    @Test
    void getById_existingEntity_returnsIt() {
        Reservation reservation = reservation("5.00", START, END, GameType.SINGLES);
        when(reservationDao.findById(1L)).thenReturn(Optional.of(reservation));

        assertThat(service.getById(1L)).isSameAs(reservation);
    }

    @Test
    void getById_missingEntity_throwsNotFound() {
        when(reservationDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getByCourtNumber_delegatesToDao() {
        List<Reservation> list = List.of(reservation("5.00", START, END, GameType.SINGLES));
        when(reservationDao.findByCourtNumber(1)).thenReturn(list);

        assertThat(service.getByCourtNumber(1)).isEqualTo(list);
    }

    @Test
    void getByCustomerPhone_delegatesToDaoWithFutureOnlyFlag() {
        List<Reservation> list = List.of(reservation("5.00", START, END, GameType.SINGLES));
        when(reservationDao.findByCustomerPhone("+420700111222", true)).thenReturn(list);

        assertThat(service.getByCustomerPhone("+420700111222", true)).isEqualTo(list);
    }

    @Test
    void delete_existingEntity_deletes() {
        when(reservationDao.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(reservationDao).deleteById(1L);
    }

    @Test
    void delete_missingEntity_throwsNotFound() {
        when(reservationDao.deleteById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(1L)).isInstanceOf(NotFoundException.class);
    }

    private static Reservation reservation(
            String pricePerMinute, LocalDateTime start, LocalDateTime end, GameType gameType) {
        SurfaceType surface = TestData.surfaceType("Clay", pricePerMinute);
        Court court = TestData.court(1, surface);
        Customer customer = TestData.customer();
        return TestData.reservation(court, customer, start, end, gameType);
    }
}
