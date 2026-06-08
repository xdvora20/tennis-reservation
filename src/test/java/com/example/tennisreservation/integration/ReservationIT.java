package com.example.tennisreservation.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.CustomerDao;
import com.example.tennisreservation.dao.ReservationDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.utils.CourtTestDataFactory;
import com.example.tennisreservation.utils.CustomerTestDataFactory;
import com.example.tennisreservation.utils.ReservationTestDataFactory;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ReservationIT {

    private static final String CREATE_BODY =
            """
            {"courtNumber":1,"startTime":"2030-01-01T10:00:00","endTime":"2030-01-01T11:00:00",
            "gameType":"SINGLES","phoneNumber":"+420700111222","customerName":"Alice"}
            """;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SurfaceTypeDao surfaceTypeDao;
    @Autowired
    private CourtDao courtDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ReservationDao reservationDao;

    private Court court;

    @BeforeEach
    void setUp() {
        court = courtDao.save(
                        CourtTestDataFactory.court(
                                1, surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType())
                        )
        );
    }

    @Test
    void create_returns201WithCalculatedPrice() throws Exception {
        mockMvc.perform(
                        post("/api/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CREATE_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courtNumber").value(1))
                .andExpect(jsonPath("$.totalPrice").value(300.00));
    }

    @Test
    void getById_returns200() throws Exception {
        Long id = persistReservation().getId();

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courtNumber").value(1));
    }

    @Test
    void update_returns200AndRecalculatesPrice() throws Exception {
        Long id = persistReservation().getId();

        mockMvc.perform(
                        put("/api/reservations/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"startTime\":\"2030-01-01T10:00:00\","
                                            + "\"endTime\":\"2030-01-01T11:00:00\",\"gameType\":\"DOUBLES\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameType").value("DOUBLES"))
                .andExpect(jsonPath("$.totalPrice").value(450.00));
    }

    @Test
    void delete_returns204() throws Exception {
        Long id = persistReservation().getId();

        mockMvc.perform(delete("/api/reservations/{id}", id)).andExpect(status().isNoContent());
    }

    @Test
    void getByCourtNumber_returns200() throws Exception {
        persistReservation();

        mockMvc.perform(get("/api/reservations/by-court/{courtNumber}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courtNumber").value(1));
    }

    @Test
    void getByCustomerPhone_returns200() throws Exception {
        persistReservation();

        mockMvc.perform(
                        get("/api/reservations/by-phone/{phone}", CustomerTestDataFactory.PHONE)
                                .param("futureOnly", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phoneNumber").value(CustomerTestDataFactory.PHONE));
    }

    private Reservation persistReservation() {
        Customer customer = customerDao.save(CustomerTestDataFactory.customer());
        return reservationDao.save(
                ReservationTestDataFactory.reservation(
                        court,
                        customer,
                        ReservationTestDataFactory.START,
                        ReservationTestDataFactory.END));
    }
}
