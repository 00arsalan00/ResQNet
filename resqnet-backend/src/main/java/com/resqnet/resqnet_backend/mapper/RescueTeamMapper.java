package com.resqnet.resqnet_backend.mapper;

import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.entity.RescueTeam;
import com.resqnet.resqnet_backend.entity.TeamStatus;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RescueTeamMapper {

    private final GeometryFactory geometryFactory;

    public RescueTeamResponseDTO toResponse(RescueTeam rescueTeam) {

        double longitude = 0;
        double latitude = 0;

        if (rescueTeam.getLocation() != null) {
            longitude = rescueTeam.getLocation().getX();
            latitude = rescueTeam.getLocation().getY();
        }

        return RescueTeamResponseDTO.builder()
                .id(rescueTeam.getId())
                .teamName(rescueTeam.getTeamName())
                .captainName(rescueTeam.getCaptainName())
                .contactInfo(rescueTeam.getContactInfo())
                .capacity(rescueTeam.getCapacity())
                .status(rescueTeam.getStatus())
                .skills(rescueTeam.getSkills())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public RescueTeam toEntity(RescueTeamRequestDTO request) {

        Point point = null;


        if (request.getLatitude() != null && request.getLongitude() != null) {
            point = geometryFactory.createPoint(
                    new Coordinate(request.getLongitude(), request.getLatitude())
            );
        }

        return RescueTeam.builder()
                .teamName(request.getTeamName())
                .captainName(request.getCaptainName())
                .contactInfo(request.getContactInfo())
                .capacity(request.getCapacity())
                .location(point)
                .status(TeamStatus.AVAILABLE)
                .skills(request.getSkills())
                .build();
    }
}