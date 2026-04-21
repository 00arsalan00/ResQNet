package com.resqnet.resqnet_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resqnet.resqnet_backend.dto.*;
import com.resqnet.resqnet_backend.entity.CampStatus;
import com.resqnet.resqnet_backend.service.ReliefCampService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReliefCampController.class)
class ReliefCampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReliefCampService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCamp_success() throws Exception {

        ReliefCampRequestDTO request = new ReliefCampRequestDTO();
        request.setName("Camp A");
        request.setCapacity(100);
        request.setLatitude(10.0);
        request.setLongitude(20.0);

        ReliefCampResponseDTO response = ReliefCampResponseDTO.builder()
                .id(UUID.randomUUID())
                .name("Camp A")
                .capacity(100)
                .occupancy(0)
                .latitude(10.0)
                .longitude(20.0)
                .status(CampStatus.ACTIVE)
                .build();

        Mockito.when(service.createCamp(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/admin/camps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Camp A"));
    }

    @Test
    void getCampById_success() throws Exception {

        UUID id = UUID.randomUUID();

        ReliefCampResponseDTO response = ReliefCampResponseDTO.builder()
                .id(id)
                .name("Camp A")
                .build();

        Mockito.when(service.getCampById(id)).thenReturn(response);

        mockMvc.perform(get("/api/admin/camps/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }
}