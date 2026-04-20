package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.VolunteerStatus;
import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class VolunteerResponseDTO {
    private UUID id;
    private String name;
    private String contactInfo;
    private double longitude;
    private double latitude;
    private LocalDateTime availabilityStart;
    private LocalDateTime availabilityEnd;
    private Set<SkillType> skills;
    private VolunteerStatus status;
}
