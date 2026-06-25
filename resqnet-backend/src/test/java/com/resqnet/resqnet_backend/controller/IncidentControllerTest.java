package com.resqnet.resqnet_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.entity.IncidentType;
import com.resqnet.resqnet_backend.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService incidentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getIncident_success() throws Exception {
        UUID id = UUID.randomUUID();

        IncidentResponseDTO response = IncidentResponseDTO.builder().build();

        when(incidentService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/incidents/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getAllIncidents_success() throws Exception {
        Page<IncidentResponseDTO> page =
                new PageImpl<>(List.of(IncidentResponseDTO.builder().build()));

        when(incidentService.getAllIncidents(any())).thenReturn(page);

        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk());
    }

    @Test
    void registerIncident_success() throws Exception {

        IncidentRequestDTO request = new IncidentRequestDTO();
        request.setType(IncidentType.FLOOD);
        request.setLatitude(28.61);
        request.setLongitude(77.20);
        request.setReporter("Arsalan");
        request.setDescription("Test description");
        request.setCity("Delhi");
        request.setDistrict("Central");
        request.setCountry("India");

        IncidentResponseDTO response = IncidentResponseDTO.builder().build();

        when(incidentService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(incidentService).register(any());
    }

    @Test
    void deleteIncident_adminOnly() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/admin/incidents/{id}", id))
                .andExpect(status().isNoContent());

        verify(incidentService).deleteIncident(id);
    }
}
