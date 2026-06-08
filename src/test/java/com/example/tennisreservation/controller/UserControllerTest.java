package com.example.tennisreservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tennisreservation.dto.CreateUserRequest;
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.facade.UserFacade;
import com.example.tennisreservation.utils.UserTestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserFacade userFacade;

    private static final String VALID_BODY =
            "{\"username\":\"alice\",\"password\":\"s3cret\",\"role\":\"USER\"}";

    @Test
    void create_validRequest_returns201WithBody() throws Exception {
        when(userFacade.create(any(CreateUserRequest.class)))
                .thenReturn(UserTestDataFactory.userResponse());

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void create_blankUsername_returns400WithValidationErrors() throws Exception {
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"\",\"password\":\"s3cret\",\"role\":\"USER\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.username").exists());
    }

    @Test
    void getAll_returns200WithList() throws Exception {
        when(userFacade.getAll()).thenReturn(List.of(UserTestDataFactory.userResponse()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("alice"));
    }

    @Test
    void getById_returns200() throws Exception {
        when(userFacade.getById(1L)).thenReturn(UserTestDataFactory.userResponse());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_missingEntity_returns404() throws Exception {
        when(userFacade.getById(99L)).thenThrow(new NotFoundException("User not found: 99"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found: 99"));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isNoContent());

        verify(userFacade).delete(1L);
    }
}
