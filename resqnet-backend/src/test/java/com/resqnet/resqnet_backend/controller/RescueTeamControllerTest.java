package com.resqnet.resqnet_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.TeamStatus;
import com.resqnet.resqnet_backend.service.RescueTeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RescueTeamController.class)
class RescueTeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RescueTeamService rescueTeamService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getTeamById() throws Exception {
        UUID id = UUID.randomUUID();

        when(rescueTeamService.getById(id))
                .thenReturn(RescueTeamResponseDTO.builder().build());

        mockMvc.perform(get("/api/teams")
                        .param("id", id.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTeams() throws Exception {
        when(rescueTeamService.getAllTeams(any()))
                .thenReturn(new PageImpl<>(List.of(RescueTeamResponseDTO.builder().build())));

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk());
    }

    @Test
    void registerTeam_admin() throws Exception {

        RescueTeamRequestDTO request = new RescueTeamRequestDTO();
        request.setTeamName("XYZ_Rescue_Team");
        request.setCapacity(5);
        request.setCaptainName("Vivek");
        request.setContactInfo("xyz@helper.com");
        request.setSkills(List.of(SkillType.RESCUE, SkillType.MEDICAL));
        request.setLatitude(28.61);
        request.setLongitude(77.20);
        request.setStatus(TeamStatus.AVAILABLE);

        when(rescueTeamService.registerTeam(any()))
                .thenReturn(RescueTeamResponseDTO.builder().build());

        mockMvc.perform(post("/api/admin/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(rescueTeamService).registerTeam(any());
    }

    @Test
    void deleteTeam_admin() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/admin/teams/{id}", id))
                .andExpect(status().isNoContent());

        verify(rescueTeamService).deleteTeam(id);
    }
}