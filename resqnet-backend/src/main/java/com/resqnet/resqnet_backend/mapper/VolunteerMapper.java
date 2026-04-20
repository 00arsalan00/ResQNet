package com.resqnet.resqnet_backend.mapper;

import com.resqnet.resqnet_backend.dto.VolunteerRequestDTO;
import com.resqnet.resqnet_backend.dto.VolunteerResponseDTO;
import com.resqnet.resqnet_backend.entity.Volunteer;
import com.resqnet.resqnet_backend.entity.VolunteerStatus;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VolunteerMapper {

    private final GeometryFactory geometryFactory;


    public VolunteerResponseDTO toResponse(Volunteer volunteer) {
        double latitude = 0;
        double longitude = 0;

        if (volunteer.getLocation() != null) {
            longitude = volunteer.getLocation().getX();
            latitude = volunteer.getLocation().getY();
        }

        return VolunteerResponseDTO.builder()
                .id(volunteer.getId())
                .name(volunteer.getName())
                .contactInfo(volunteer.getContactInfo())
                .availabilityStart(volunteer.getAvailabilityStart())
                .availabilityEnd(volunteer.getAvailabilityEnd())
                .skills(volunteer.getSkills())
                .status(volunteer.getStatus())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }


    public Volunteer toEntity(VolunteerRequestDTO request) {

        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );

        return Volunteer.builder()
                .name(request.getName())
                .contactInfo(request.getContactInfo())
                .skills(request.getSkills())
                .status(request.getStatus() != null
                        ? request.getStatus()
                        : VolunteerStatus.AVAILABLE)
                .availabilityStart(request.getAvailabilityStart())
                .availabilityEnd(request.getAvailabilityEnd())
                .location(point)
                .build();
    }
}