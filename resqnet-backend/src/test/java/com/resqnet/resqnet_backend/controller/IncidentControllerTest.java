package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.entity.IncidentStatus;
import com.resqnet.resqnet_backend.entity.IncidentType;
import com.resqnet.resqnet_backend.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService incidentService;

    @Test
    void shouldCreateIncident() throws Exception {

        String requestJson = """
                {
                  "type": "FLOOD",
                  "severity": 3,
                  "latitude": 28.61,
                  "longitude": 77.20,
                  "reporter": "Arsalan"
                }
                """;

        IncidentResponseDTO response = IncidentResponseDTO.builder()
                .id(UUID.randomUUID())
                .type(IncidentType.FLOOD)
                .severity(3)
                .status(IncidentStatus.REPORTED)
                .latitude(28.61)
                .longitude(77.20)
                .reporter("Arsalan")
                .build();

        when(incidentService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("FLOOD"));
    }
}

