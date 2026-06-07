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

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.facade.CourtFacade;
import com.example.tennisreservation.utils.CourtTestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourtController.class)
class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CourtFacade courtFacade;

    private static final String VALID_BODY = "{\"courtNumber\":1,\"surfaceTypeId\":1}";

    @Test
    void create_validRequest_returns201WithBody() throws Exception {
        when(courtFacade.create(any(CourtRequest.class)))
                .thenReturn(CourtTestDataFactory.courtResponse());

        mockMvc.perform(
                        post("/api/courts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courtNumber").value(1))
                .andExpect(jsonPath("$.surfaceType.name").value("Clay"));
    }

    @Test
    void create_missingSurfaceTypeId_returns400() throws Exception {
        mockMvc.perform(
                        post("/api/courts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"courtNumber\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_returns200WithList() throws Exception {
        when(courtFacade.getAll()).thenReturn(List.of(CourtTestDataFactory.courtResponse()));

        mockMvc.perform(get("/api/courts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courtNumber").value(1));
    }

    @Test
    void getById_returns200() throws Exception {
        when(courtFacade.getById(1L)).thenReturn(CourtTestDataFactory.courtResponse());

        mockMvc.perform(get("/api/courts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_validRequest_returns200() throws Exception {
        when(courtFacade.update(eq(1L), any(CourtRequest.class)))
                .thenReturn(CourtTestDataFactory.courtResponse());

        mockMvc.perform(
                        put("/api/courts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_BODY))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/courts/1")).andExpect(status().isNoContent());

        verify(courtFacade).delete(1L);
    }

    @Test
    void getById_missingEntity_returns404() throws Exception {
        when(courtFacade.getById(99L)).thenThrow(new NotFoundException("Court not found: 99"));

        mockMvc.perform(get("/api/courts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Court not found: 99"));
    }
}
