package com.resqnet.resqnet_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resqnet.resqnet_backend.dto.*;
import com.resqnet.resqnet_backend.entity.ResourceType;
import com.resqnet.resqnet_backend.service.ResourceService;
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

@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createResource_success() throws Exception {

        ResourceRequestDTO request = new ResourceRequestDTO();
        request.setType(ResourceType.FOOD);
        request.setQuantity(100);
        request.setAvailableQuantity(100);
        request.setLatitude(10.0);
        request.setLongitude(20.0);

        ResourceResponseDTO response = ResourceResponseDTO.builder()
                .id(UUID.randomUUID())
                .type(ResourceType.FOOD)
                .quantity(100)
                .availableQuantity(100)
                .latitude(10.0)
                .longitude(20.0)
                .build();

        Mockito.when(service.createResource(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("FOOD"));
    }
}
