package com.example.tennisreservation.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
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
class SurfaceTypeIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SurfaceTypeDao surfaceTypeDao;

    @Test
    void create_returns201() throws Exception {
        mockMvc.perform(
                        post("/api/surface-types")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Clay\",\"pricePerMinute\":5.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Clay"));
    }

    @Test
    void getAll_returns200() throws Exception {
        surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType());

        mockMvc.perform(get("/api/surface-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Clay"));
    }

    @Test
    void getById_returns200() throws Exception {
        Long id = surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType()).getId();

        mockMvc.perform(get("/api/surface-types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Clay"));
    }

    @Test
    void update_returns200() throws Exception {
        Long id = surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType()).getId();

        mockMvc.perform(
                        put("/api/surface-types/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Grass\",\"pricePerMinute\":7.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Grass"));
    }

    @Test
    void delete_returns204() throws Exception {
        Long id = surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType()).getId();

        mockMvc.perform(delete("/api/surface-types/{id}", id)).andExpect(status().isNoContent());
    }

    @Test
    void delete_surfaceTypeUsedByCourt_returns409() throws Exception {
        Long surfaceId = surfaceTypeDao.save(SurfaceTypeTestDataFactory.surfaceType()).getId();
        mockMvc.perform(
                        post("/api/courts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"courtNumber\":1,\"surfaceTypeId\":" + surfaceId + "}"))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/surface-types/{id}", surfaceId))
                .andExpect(status().isConflict());
    }
}
