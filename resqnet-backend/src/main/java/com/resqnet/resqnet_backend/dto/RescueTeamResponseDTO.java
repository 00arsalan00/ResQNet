package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.TeamStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RescueTeamResponseDTO {

    private UUID id;

    private String teamName;

    private String captainName;

    private String contactInfo;

    private Integer capacity;

    private TeamStatus status;

    private List<SkillType> skills;

    private Double latitude;

    private Double longitude;
}