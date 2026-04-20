package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.VolunteerStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class VolunteerRequestDTO {
    @NotBlank(message = "Volunteer name is required")
    private String name;

    @NotBlank(message = "Contact info is required")
    private String contactInfo;

    @NotEmpty(message = "At least one skill is required")
    private Set<SkillType> skills;

    @NotNull
    private VolunteerStatus status;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double longitude;

    @NotNull
    private LocalDateTime availabilityStart;

    @NotNull
    private LocalDateTime availabilityEnd;
}
