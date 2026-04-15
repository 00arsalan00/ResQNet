package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.AssignmentStatus;
import com.resqnet.resqnet_backend.entity.Incident;
import com.resqnet.resqnet_backend.entity.RescueTeam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AssignmentRequestDTO {

    @NotNull(message = "Incident ID is required")
    private UUID incidentId;

    @NotNull(message = "Team ID is required")
    private UUID teamId;

    private AssignmentStatus status;
}
