package com.resqnet.resqnet_backend.mapper;

import com.resqnet.resqnet_backend.dto.ResourceRequestDTO;
import com.resqnet.resqnet_backend.dto.ResourceResponseDTO;
import com.resqnet.resqnet_backend.entity.Resource;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResourceMapper {

    private final GeometryFactory geometryFactory;

    public ResourceResponseDTO toResponse(Resource resource) {

        Double latitude = null;
        Double longitude = null;

        if (resource.getWarehouseLocation() != null) {
            longitude = resource.getWarehouseLocation().getX();
            latitude = resource.getWarehouseLocation().getY();
        }

        return ResourceResponseDTO.builder()
                .id(resource.getId())
                .type(resource.getType())
                .quantity(resource.getTotalQuantity())
                .availableQuantity(resource.getAvailableQuantity())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public Resource toEntity(ResourceRequestDTO request) {

        if (request.getLatitude() == null || request.getLongitude() == null) {
            throw new IllegalArgumentException("Latitude and Longitude must not be null");
        }

        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );

        return Resource.builder()
                .type(request.getType())
                .totalQuantity(request.getQuantity())
                .availableQuantity(request.getQuantity())
                .warehouseLocation(point)
                .build();
    }

    public void updateEntity(Resource resource, ResourceRequestDTO request) {

        resource.setType(request.getType());
        resource.setTotalQuantity(request.getQuantity());
        resource.setAvailableQuantity(request.getAvailableQuantity());

        if (request.getLatitude() != null && request.getLongitude() != null) {
            Point point = geometryFactory.createPoint(
                    new Coordinate(request.getLongitude(), request.getLatitude())
            );
            resource.setWarehouseLocation(point);
        }
    }
}