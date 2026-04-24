package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.dto.AssignmentRequestDTO;
import com.resqnet.resqnet_backend.dto.AssignmentResponseDTO;
import com.resqnet.resqnet_backend.entity.IncidentAssignment;
import com.resqnet.resqnet_backend.mapper.AssignmentMapper;
import com.resqnet.resqnet_backend.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final AssignmentMapper assignmentMapper;

    @PostMapping("/{incidentId}/assign/{teamId}")
    public ResponseEntity<AssignmentResponseDTO> assign(
            @PathVariable UUID incidentId,
            @PathVariable UUID teamId
    ) {
        IncidentAssignment assignment = assignmentService.assignTeam(incidentId, teamId);
        return ResponseEntity.status(201)
                .body(assignmentMapper.toResponse(assignment));
    }

    @GetMapping("/assignments")
    public ResponseEntity<Page<AssignmentResponseDTO>> getAllAssignments(Pageable pageable) {
        return ResponseEntity.ok(assignmentService.getAllAssignments(pageable));
    }

    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentById(@PathVariable UUID assignmentId) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(assignmentId));
    }

    @GetMapping("/incidents/{incidentId}/assignments")
    public ResponseEntity<Page<AssignmentResponseDTO>> getAssignmentsByIncidentId(
            @PathVariable UUID incidentId,Pageable pageable) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByIncidentId(incidentId,pageable));
    }

    @GetMapping("/teams/{teamId}/assignments")
    public ResponseEntity<Page<AssignmentResponseDTO>> getAssignmentsByTeamId(
            @PathVariable UUID teamId,Pageable pageable) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByTeamId(teamId,pageable));
    }


    @PutMapping("/assignments/{assignmentId}/status")
    public ResponseEntity<AssignmentResponseDTO> updateAssignment(
            @PathVariable UUID assignmentId,
            @Valid @RequestBody AssignmentRequestDTO request) {
        return ResponseEntity.ok(assignmentService.updateAssignment(assignmentId, request));
    }

    @DeleteMapping("/assignments/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable UUID assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }



}
