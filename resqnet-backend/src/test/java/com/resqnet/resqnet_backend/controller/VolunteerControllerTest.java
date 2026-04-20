package com.resqnet.resqnet_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resqnet.resqnet_backend.dto.VolunteerRequestDTO;
import com.resqnet.resqnet_backend.dto.VolunteerResponseDTO;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.VolunteerStatus;
import com.resqnet.resqnet_backend.service.VolunteerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VolunteerController.class)
class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerService volunteerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterVolunteer() throws Exception {

        VolunteerRequestDTO request = new VolunteerRequestDTO();
        request.setName("Test");
        request.setContactInfo("test@example.com");
        request.setSkills(Set.of(SkillType.FIRE_FIGHTING));
        request.setStatus(VolunteerStatus.AVAILABLE);
        request.setLatitude(28.61);
        request.setLongitude(77.20);
        request.setAvailabilityStart(LocalDateTime.now());
        request.setAvailabilityEnd(LocalDateTime.now().plusHours(5));

        VolunteerResponseDTO response = VolunteerResponseDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .contactInfo("test@example.com")
                .skills(Set.of(SkillType.FIRE_FIGHTING))
                .status(VolunteerStatus.AVAILABLE)
                .latitude(28.61)
                .longitude(77.20)
                .availabilityStart(LocalDateTime.now())
                .availabilityEnd(LocalDateTime.now().plusHours(5))
                .build();

        Mockito.when(volunteerService.registerVolunteer(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/volunteers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"));
    }
}