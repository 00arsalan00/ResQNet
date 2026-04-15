package com.resqnet.resqnet_backend.mapper;

import com.resqnet.resqnet_backend.dto.AssignmentResponseDTO;
import com.resqnet.resqnet_backend.entity.IncidentAssignment;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {

    public AssignmentResponseDTO toResponse(IncidentAssignment assignment) {
        return AssignmentResponseDTO.builder()
                .id(assignment.getId())
                .incidentId(assignment.getIncident().getId())
                .incidentType(assignment.getIncident().getType())
                .teamId(assignment.getRescueTeam().getId())
                .teamName(assignment.getRescueTeam().getTeamName())
                .status(assignment.getStatus())
                .assignedAt(assignment.getAssignedAt())
                .build();
    }
}
