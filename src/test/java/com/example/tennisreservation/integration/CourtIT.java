package com.example.tennisreservation.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.utils.CourtTestDataFactory;
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
class CourtIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private SurfaceTypeDao surfaceTypeDao;
    @Autowired private CourtDao courtDao;

    private SurfaceType surface;

    @BeforeEach
    void setUp() {
        surface = surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType());
    }

    @Test
    void create_returns201() throws Exception {
        mockMvc.perform(
                        post("/api/courts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"courtNumber\":1,\"surfaceTypeId\":"
                                                + surface.getId()
                                                + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courtNumber").value(1))
                .andExpect(jsonPath("$.surfaceType.name").value("Clay"));
    }

    @Test
    void getAll_returns200() throws Exception {
        courtDao.save(CourtTestDataFactory.court(1, surface));

        mockMvc.perform(get("/api/courts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courtNumber").value(1));
    }

    @Test
    void getById_returns200() throws Exception {
        Long id = courtDao.save(CourtTestDataFactory.court(1, surface)).getId();

        mockMvc.perform(get("/api/courts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courtNumber").value(1));
    }

    @Test
    void update_returns200() throws Exception {
        Long id = courtDao.save(CourtTestDataFactory.court(1, surface)).getId();

        mockMvc.perform(
                        put("/api/courts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"courtNumber\":2,\"surfaceTypeId\":"
                                                + surface.getId()
                                                + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courtNumber").value(2));
    }

    @Test
    void delete_returns204() throws Exception {
        Long id = courtDao.save(CourtTestDataFactory.court(1, surface)).getId();

        mockMvc.perform(delete("/api/courts/{id}", id)).andExpect(status().isNoContent());
    }
}
