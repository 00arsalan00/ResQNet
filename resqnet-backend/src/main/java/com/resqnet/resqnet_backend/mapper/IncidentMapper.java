package com.resqnet.resqnet_backend.mapper;

import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.entity.Incident;
import com.resqnet.resqnet_backend.entity.IncidentStatus;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncidentMapper {

    public IncidentResponseDTO toResponse(Incident incident) {

        double latitude = 0;
        double longitude = 0;

        if (incident.getLocation() != null) {
            longitude = incident.getLocation().getX();
            latitude = incident.getLocation().getY();
        }

        return IncidentResponseDTO.builder()
                .id(incident.getId())
                .type(incident.getType())
                .severity(incident.getSeverity())
                .status(incident.getStatus())
                .latitude(latitude)
                .longitude(longitude)
                .reporter(incident.getReporter())
                .build();
    }

    public Incident toEntity(IncidentRequestDTO request, GeometryFactory geometryFactory) {

        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );

        return Incident.builder()
                .type(request.getType())
                .severity(request.getSeverity())
                .location(point)
                .status(IncidentStatus.REPORTED)
                .reporter(request.getReporter())
                .build();
    }
}