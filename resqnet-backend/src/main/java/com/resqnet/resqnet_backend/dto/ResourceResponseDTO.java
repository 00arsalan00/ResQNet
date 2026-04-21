package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.ResourceType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ResourceResponseDTO {

    private UUID id;

    private ResourceType type;

    private Integer totalQuantity;

    private Integer availableQuantity;

    private Double latitude;

    private Double longitude;
}