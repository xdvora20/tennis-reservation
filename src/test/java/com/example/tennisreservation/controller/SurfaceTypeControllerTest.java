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

import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.facade.SurfaceTypeFacade;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SurfaceTypeController.class)
class SurfaceTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SurfaceTypeFacade surfaceTypeFacade;

    private static final String VALID_BODY = "{\"name\":\"Clay\",\"pricePerMinute\":5.00}";

    @Test
    void create_validRequest_returns201WithBody() throws Exception {
        when(surfaceTypeFacade.create(any(SurfaceTypeRequest.class)))
                .thenReturn(SurfaceTypeTestDataFactory.surfaceTypeResponse());

        mockMvc.perform(
                        post("/api/surface-types")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Clay"))
                .andExpect(jsonPath("$.pricePerMinute").value(5.00));
    }

    @Test
    void create_blankName_returns400WithValidationErrors() throws Exception {
        mockMvc.perform(
                        post("/api/surface-types")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"pricePerMinute\":5.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.name").exists());
    }

    @Test
    void getById_missingEntity_returns404WithErrorBody() throws Exception {
        when(surfaceTypeFacade.getById(99L))
                .thenThrow(new NotFoundException("Surface type not found: 99"));

        mockMvc.perform(get("/api/surface-types/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Surface type not found: 99"))
                .andExpect(jsonPath("$.path").value("/api/surface-types/99"));
    }

    @Test
    void getAll_returns200WithList() throws Exception {
        when(surfaceTypeFacade.getAll())
                .thenReturn(List.of(SurfaceTypeTestDataFactory.surfaceTypeResponse()));

        mockMvc.perform(get("/api/surface-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Clay"));
    }

    @Test
    void getById_returns200() throws Exception {
        when(surfaceTypeFacade.getById(1L))
                .thenReturn(SurfaceTypeTestDataFactory.surfaceTypeResponse());

        mockMvc.perform(get("/api/surface-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_validRequest_returns200() throws Exception {
        when(surfaceTypeFacade.update(eq(1L), any(SurfaceTypeRequest.class)))
                .thenReturn(SurfaceTypeTestDataFactory.surfaceTypeResponse());

        mockMvc.perform(
                        put("/api/surface-types/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_BODY))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/surface-types/1")).andExpect(status().isNoContent());

        verify(surfaceTypeFacade).delete(1L);
    }
}
