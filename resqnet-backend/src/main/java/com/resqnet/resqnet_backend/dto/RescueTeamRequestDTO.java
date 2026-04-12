package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.SkillType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class RescueTeamRequestDTO {

    @NotBlank(message = "Team name is required")
    private String teamName;

    @NotBlank(message = "Captain name is required")
    private String captainName;

    @NotBlank(message = "Contact info is required")
    private String contactInfo;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotEmpty(message = "At least one skill is required")
    private List<SkillType> skills;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double longitude;
}