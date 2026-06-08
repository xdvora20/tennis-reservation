package com.example.tennisreservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tennisreservation.exception.UnauthorizedException;
import com.example.tennisreservation.facade.AuthFacade;
import com.example.tennisreservation.utils.AuthTestDataFactory;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthFacade authFacade;

    @Test
    void login_validCredentials_returns200WithTokenAndAuthorizationHeader() throws Exception {
        when(authFacade.login("alice", "s3cret")).thenReturn(AuthTestDataFactory.tokenResponse());

        mockMvc.perform(
                        post("/api/auth/login")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        AuthTestDataFactory.basicAuthHeader("alice", "s3cret")))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer access"))
                .andExpect(jsonPath("$.accessToken").value("access"))
                .andExpect(jsonPath("$.refreshToken").value("refresh"));
    }

    @Test
    void login_missingHeader_returns401() throws Exception {
        mockMvc.perform(post("/api/auth/login")).andExpect(status().isUnauthorized());
    }

    @Test
    void login_nonBasicScheme_returns401() throws Exception {
        mockMvc.perform(post("/api/auth/login").header(HttpHeaders.AUTHORIZATION, "Bearer something"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_malformedBase64_returns401() throws Exception {
        mockMvc.perform(post("/api/auth/login").header(HttpHeaders.AUTHORIZATION, "Basic !!!notbase64"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_headerWithoutColon_returns401() throws Exception {
        String noColon =
                "Basic "
                        + Base64.getEncoder()
                                .encodeToString("nocolon".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(post("/api/auth/login").header(HttpHeaders.AUTHORIZATION, noColon))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        when(authFacade.login(any(), any()))
                .thenThrow(new UnauthorizedException("Invalid username or password"));

        mockMvc.perform(
                        post("/api/auth/login")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        AuthTestDataFactory.basicAuthHeader("alice", "wrong")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }
}
