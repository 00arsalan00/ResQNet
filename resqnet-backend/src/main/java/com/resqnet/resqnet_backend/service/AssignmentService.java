package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.AssignmentRequestDTO;
import com.resqnet.resqnet_backend.dto.AssignmentResponseDTO;
import com.resqnet.resqnet_backend.entity.IncidentAssignment;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AssignmentService {

    IncidentAssignment assignTeam(UUID incidentId, UUID teamId);

    Page<AssignmentResponseDTO> getAllAssignments(Pageable pageable);

    AssignmentResponseDTO getAssignmentById(UUID assignmentId);

    Page<AssignmentResponseDTO> getAssignmentsByIncidentId(UUID incidentId, Pageable pageable);

    Page<AssignmentResponseDTO> getAssignmentsByTeamId(UUID teamId, Pageable pageable);

    AssignmentResponseDTO updateAssignment(UUID assignmentId, @Valid AssignmentRequestDTO request);

    void deleteAssignment(UUID assignmentId);
}
