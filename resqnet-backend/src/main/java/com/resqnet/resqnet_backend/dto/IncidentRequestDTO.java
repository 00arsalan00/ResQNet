package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.IncidentStatus;
import com.resqnet.resqnet_backend.entity.IncidentType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

@Data
public class IncidentRequestDTO {
    @NotNull
    private IncidentType type;
    @NotNull
    private Integer severity;

    @NotNull
    private String reporter;
    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;
}
