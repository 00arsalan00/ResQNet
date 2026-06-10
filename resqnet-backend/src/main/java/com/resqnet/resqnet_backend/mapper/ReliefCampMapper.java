package com.resqnet.resqnet_backend.mapper;

import com.resqnet.resqnet_backend.dto.ReliefCampRequestDTO;
import com.resqnet.resqnet_backend.dto.ReliefCampResponseDTO;
import com.resqnet.resqnet_backend.entity.CampStatus;
import com.resqnet.resqnet_backend.entity.ReliefCamp;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReliefCampMapper {

    private final GeometryFactory geometryFactory;

    public ReliefCampResponseDTO toResponse(ReliefCamp camp) {

        Double latitude = null;
        Double longitude = null;

        if (camp.getLocation() != null) {
            longitude = camp.getLocation().getX();
            latitude = camp.getLocation().getY();
        }

        UUID incidentId = camp.getIncident() != null ? camp.getIncident().getId() : null;

        return ReliefCampResponseDTO.builder()
                .id(camp.getId())
                .name(camp.getName())
                .capacity(camp.getCapacity())
                .occupancy(camp.getOccupancy())
                .latitude(latitude)
                .longitude(longitude)
                .status(camp.getStatus())
                .incidentId(incidentId)
                .build();
    }

    public ReliefCamp toEntity(ReliefCampRequestDTO request) {

        if (request.getLatitude() == null || request.getLongitude() == null) {
            throw new IllegalArgumentException("Latitude and Longitude must not be null");
        }

        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );

        return ReliefCamp.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .occupancy(0)
                .location(point)
                .status(CampStatus.ACTIVE)
                .build();
    }

    public void updateEntity(ReliefCamp camp, ReliefCampRequestDTO request) {

        camp.setName(request.getName());
        camp.setCapacity(request.getCapacity());

        if (request.getLatitude() != null && request.getLongitude() != null) {
            Point point = geometryFactory.createPoint(
                    new Coordinate(request.getLongitude(), request.getLatitude())
            );
            camp.setLocation(point);
        }
    }
}