package com.example.tennisreservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tennisreservation.dto.CreateReservationRequest;
import com.example.tennisreservation.dto.UpdateReservationRequest;
import com.example.tennisreservation.exception.BadRequestException;
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.facade.ReservationFacade;
import com.example.tennisreservation.utils.CustomerTestDataFactory;
import com.example.tennisreservation.utils.ReservationTestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ReservationFacade reservationFacade;

    private static final String CREATE_BODY =
            """
            {"courtNumber":1,"startTime":"2030-01-01T10:00:00","endTime":"2030-01-01T11:00:00",
            "gameType":"SINGLES","phoneNumber":"+420700111222","customerName":"Alice"}
            """;

    @Test
    void create_validRequest_returns201WithPrice() throws Exception {
        when(reservationFacade.create(any(CreateReservationRequest.class)))
                .thenReturn(ReservationTestDataFactory.reservationResponse());

        mockMvc.perform(
                        post("/api/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CREATE_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(300.00))
                .andExpect(jsonPath("$.courtNumber").value(1));
    }

    @Test
    void create_missingPhoneNumber_returns400() throws Exception {
        mockMvc.perform(
                        post("/api/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"courtNumber\":1,\"startTime\":\"2030-01-01T10:00:00\","
                                            + "\"endTime\":\"2030-01-01T11:00:00\",\"gameType\":\"SINGLES\","
                                            + "\"customerName\":\"Alice\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_overlappingReservation_returns400() throws Exception {
        when(reservationFacade.create(any(CreateReservationRequest.class)))
                .thenThrow(new BadRequestException("Reservation overlaps an existing reservation"));

        mockMvc.perform(
                        post("/api/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CREATE_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Reservation overlaps an existing reservation"));
    }

    @Test
    void getById_returns200() throws Exception {
        when(reservationFacade.getById(1L))
                .thenReturn(ReservationTestDataFactory.reservationResponse());

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_missingEntity_returns404() throws Exception {
        when(reservationFacade.getById(99L))
                .thenThrow(new NotFoundException("Reservation not found: 99"));

        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Reservation not found: 99"));
    }

    @Test
    void update_validRequest_returns200() throws Exception {
        when(reservationFacade.update(eq(1L), any(UpdateReservationRequest.class)))
                .thenReturn(ReservationTestDataFactory.reservationResponse());

        mockMvc.perform(
                        put("/api/reservations/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"startTime\":\"2030-01-01T10:00:00\","
                                            + "\"endTime\":\"2030-01-01T11:00:00\",\"gameType\":\"SINGLES\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/reservations/1")).andExpect(status().isNoContent());

        verify(reservationFacade).delete(1L);
    }

    @Test
    void getByCourtNumber_returns200WithList() throws Exception {
        when(reservationFacade.getByCourtNumber(1))
                .thenReturn(List.of(ReservationTestDataFactory.reservationResponse()));

        mockMvc.perform(get("/api/reservations/by-court/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courtNumber").value(1));
    }

    @Test
    void getByCustomerPhone_passesFutureOnlyFlag() throws Exception {
        when(reservationFacade.getByCustomerPhone(eq(CustomerTestDataFactory.PHONE), eq(true)))
                .thenReturn(List.of(ReservationTestDataFactory.reservationResponse()));

        mockMvc.perform(
                        get("/api/reservations/by-phone/{phone}", CustomerTestDataFactory.PHONE)
                                .param("futureOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phoneNumber").value(CustomerTestDataFactory.PHONE));

        verify(reservationFacade).getByCustomerPhone(CustomerTestDataFactory.PHONE, true);
    }
}
