package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.IncidentStatus;
import com.resqnet.resqnet_backend.entity.IncidentType;
import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import java.util.UUID;

@Data
@Builder
public class IncidentResponseDTO {
    private UUID id;
    private IncidentType type;
    private Integer severity;
    private IncidentStatus status;
    private String reporter;
    private double latitude;
    private double longitude;

}
