package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseDTO {

    private UUID id;

    private UUID incidentId;
    private IncidentType incidentType;

    private UUID teamId;
    private String teamName;

    private AssignmentStatus status;

    private LocalDateTime assignedAt;
}
