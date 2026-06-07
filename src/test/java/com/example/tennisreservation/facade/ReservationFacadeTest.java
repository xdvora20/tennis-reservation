package com.example.tennisreservation.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dto.CreateReservationRequest;
import com.example.tennisreservation.dto.ReservationResponse;
import com.example.tennisreservation.dto.UpdateReservationRequest;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.entity.GameType;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.mapper.ReservationMapper;
import com.example.tennisreservation.service.CourtService;
import com.example.tennisreservation.service.CustomerService;
import com.example.tennisreservation.service.ReservationService;
import com.example.tennisreservation.utils.CourtTestDataFactory;
import com.example.tennisreservation.utils.CustomerTestDataFactory;
import com.example.tennisreservation.utils.ReservationTestDataFactory;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @Mock
    private ReservationService reservationService;
    @Mock
    private CourtService courtService;
    @Mock
    private CustomerService customerService;
    @Mock
    private ReservationMapper reservationMapper;
    @InjectMocks
    private ReservationFacade facade;

    private final ReservationResponse response = ReservationTestDataFactory.reservationResponse();

    @Test
    void create_resolvesCourtAndCustomerAssemblesReservationAndReturnsResponse() {
        CreateReservationRequest request = ReservationTestDataFactory.createReservationRequest();
        Court court = CourtTestDataFactory.court(1, SurfaceTypeTestDataFactory.surfaceType());
        Customer customer = CustomerTestDataFactory.customer();
        Reservation saved = ReservationTestDataFactory.reservation();
        when(courtService.getByNumber(request.courtNumber())).thenReturn(court);
        when(customerService.getOrCreate(request.phoneNumber(), request.customerName()))
                .thenReturn(customer);
        when(reservationService.create(any(Reservation.class))).thenReturn(saved);
        when(reservationMapper.toResponse(saved)).thenReturn(response);

        assertThat(facade.create(request)).isSameAs(response);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationService).create(captor.capture());
        Reservation passed = captor.getValue();
        assertThat(passed.getCourt()).isSameAs(court);
        assertThat(passed.getCustomer()).isSameAs(customer);
        assertThat(passed.getStartTime()).isEqualTo(request.startTime());
        assertThat(passed.getEndTime()).isEqualTo(request.endTime());
        assertThat(passed.getGameType()).isEqualTo(request.gameType());
    }

    @Test
    void update_loadsAppliesRequestChangesAndReturnsResponse() {
        UpdateReservationRequest request = ReservationTestDataFactory.updateReservationRequest();
        Reservation existing =
                ReservationTestDataFactory.reservation(
                        CourtTestDataFactory.court(1, SurfaceTypeTestDataFactory.surfaceType()),
                        CustomerTestDataFactory.customer(),
                        LocalDateTime.of(2030, 1, 1, 14, 0),
                        LocalDateTime.of(2030, 1, 1, 15, 0),
                        GameType.DOUBLES);
        when(reservationService.getById(1L)).thenReturn(existing);
        when(reservationService.update(existing)).thenReturn(existing);
        when(reservationMapper.toResponse(existing)).thenReturn(response);

        assertThat(facade.update(1L, request)).isSameAs(response);
        assertThat(existing.getStartTime()).isEqualTo(request.startTime());
        assertThat(existing.getEndTime()).isEqualTo(request.endTime());
        assertThat(existing.getGameType()).isEqualTo(request.gameType());
    }

    @Test
    void getById_returnsMappedResponse() {
        Reservation reservation = ReservationTestDataFactory.reservation();
        when(reservationService.getById(1L)).thenReturn(reservation);
        when(reservationMapper.toResponse(reservation)).thenReturn(response);

        assertThat(facade.getById(1L)).isSameAs(response);
    }

    @Test
    void getByCourtNumber_returnsMappedResponses() {
        List<Reservation> reservations = List.of(ReservationTestDataFactory.reservation());
        List<ReservationResponse> responses = List.of(response);
        when(reservationService.getByCourtNumber(1)).thenReturn(reservations);
        when(reservationMapper.toResponseList(reservations)).thenReturn(responses);

        assertThat(facade.getByCourtNumber(1)).isEqualTo(responses);
    }

    @Test
    void getByCustomerPhone_passesFutureOnlyFlagAndReturnsMappedResponses() {
        List<Reservation> reservations = List.of(ReservationTestDataFactory.reservation());
        List<ReservationResponse> responses = List.of(response);
        when(reservationService.getByCustomerPhone(CustomerTestDataFactory.PHONE, true)).thenReturn(reservations);
        when(reservationMapper.toResponseList(reservations)).thenReturn(responses);

        assertThat(facade.getByCustomerPhone(CustomerTestDataFactory.PHONE, true)).isEqualTo(responses);
    }

    @Test
    void delete_delegatesToService() {
        facade.delete(1L);

        verify(reservationService).delete(1L);
    }
}
