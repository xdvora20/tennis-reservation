package com.example.tennisreservation.integration;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.security.JwtService;
import com.example.tennisreservation.service.UserService;
import com.example.tennisreservation.utils.AuthTestDataFactory;
import com.example.tennisreservation.utils.CourtTestDataFactory;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import com.example.tennisreservation.utils.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private SurfaceTypeDao surfaceTypeDao;
    @Autowired
    private CourtDao courtDao;

    private SurfaceType surface;
    private Court court;

    @BeforeEach
    void setUp() {
        surface = surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType());
        court = courtDao.save(CourtTestDataFactory.court(1, surface));
    }

    private String bearer(Role role) {
        return "Bearer " + jwtService.generateAccessToken(UserTestDataFactory.user("tester", "hash", role));
    }

    @Test
    void protectedEndpoint_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/courts")).andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_withInvalidToken_returns401() throws Exception {
        mockMvc.perform(get("/api/courts").header(HttpHeaders.AUTHORIZATION, "Bearer not-a-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_withRefreshToken_returns401() throws Exception {
        String refreshToken = jwtService.generateRefreshToken(UserTestDataFactory.user());

        mockMvc.perform(get("/api/courts").header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_validCredentials_returns200WithTokenInBodyAndHeader() throws Exception {
        userService.create("admin", "admin123", Role.ADMIN);

        mockMvc.perform(
                        post("/api/auth/login")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        AuthTestDataFactory.basicAuthHeader("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, startsWith("Bearer ")))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {
        userService.create("admin", "admin123", Role.ADMIN);

        mockMvc.perform(
                        post("/api/auth/login")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        AuthTestDataFactory.basicAuthHeader("admin", "wrong")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userRole_canReadCourts_returns200() throws Exception {
        mockMvc.perform(get("/api/courts").header(HttpHeaders.AUTHORIZATION, bearer(Role.USER)))
                .andExpect(status().isOk());
    }

    @Test
    void userRole_cannotCreateCourt_returns403() throws Exception {
        mockMvc.perform(
                        post("/api/courts")
                                .header(HttpHeaders.AUTHORIZATION, bearer(Role.USER))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"courtNumber\":2,\"surfaceTypeId\":" + surface.getId() + "}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminRole_canCreateCourt_returns201() throws Exception {
        mockMvc.perform(
                        post("/api/courts")
                                .header(HttpHeaders.AUTHORIZATION, bearer(Role.ADMIN))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"courtNumber\":2,\"surfaceTypeId\":" + surface.getId() + "}"))
                .andExpect(status().isCreated());
    }

    @Test
    void userRole_canCreateReservation_returns201() throws Exception {
        String body =
                """
                {"courtNumber":%d,"startTime":"2030-01-01T10:00:00","endTime":"2030-01-01T11:00:00",
                "gameType":"SINGLES","phoneNumber":"+420700111222","customerName":"Alice"}
                """
                        .formatted(court.getCourtNumber());

        mockMvc.perform(
                        post("/api/reservations")
                                .header(HttpHeaders.AUTHORIZATION, bearer(Role.USER))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated());
    }
}
