package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.CampStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReliefCampResponseDTO {

    private UUID id;

    private String name;

    private Integer capacity;

    private Integer occupancy;

    private Double latitude;

    private Double longitude;

    private CampStatus status;
}