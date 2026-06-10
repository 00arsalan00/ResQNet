package com.resqnet.resqnet_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resqnet.resqnet_backend.dto.AssignmentRequestDTO;
import com.resqnet.resqnet_backend.dto.AssignmentResponseDTO;
import com.resqnet.resqnet_backend.entity.AssignmentStatus;
import com.resqnet.resqnet_backend.entity.IncidentAssignment;
import com.resqnet.resqnet_backend.mapper.AssignmentMapper;
import com.resqnet.resqnet_backend.service.AssignmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssignmentController.class)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignmentService assignmentService;

    @MockBean
    private AssignmentMapper assignmentMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void assign_success() throws Exception {

        UUID incidentId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        IncidentAssignment assignment = new IncidentAssignment();

        when(assignmentService.assignTeam(incidentId, teamId))
                .thenReturn(assignment);
        when(assignmentMapper.toResponse(assignment))
                .thenReturn(AssignmentResponseDTO.builder().build());

        mockMvc.perform(post("/api/admin/{incidentId}/assign/{teamId}",
                        incidentId, teamId))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllAssignments_success() throws Exception {

        Page<AssignmentResponseDTO> page =
                new PageImpl<>(List.of(AssignmentResponseDTO.builder().build()));

        when(assignmentService.getAllAssignments(any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/admin/assignments"))
                .andExpect(status().isOk());
    }

    @Test
    void updateAssignment_success() throws Exception {

        UUID assignmentId = UUID.randomUUID();

        AssignmentRequestDTO request = new AssignmentRequestDTO();

        request.setIncidentId(UUID.randomUUID());
        request.setTeamId(UUID.randomUUID());
        request.setStatus(AssignmentStatus.ASSIGNED);

        when(assignmentService.updateAssignment(eq(assignmentId), any()))
                .thenReturn(AssignmentResponseDTO.builder().build());

        mockMvc.perform(put("/api/admin/assignments/{id}/status", assignmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAssignment_success() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/admin/assignments/{id}", id))
                .andExpect(status().isNoContent());

        verify(assignmentService).deleteAssignment(id);
    }
}