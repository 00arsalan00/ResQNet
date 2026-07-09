package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.IncidentNotFoundException;
import com.resqnet.resqnet_backend.mapper.IncidentMapper;
import com.resqnet.resqnet_backend.repository.IncidentRepository;
import com.resqnet.resqnet_backend.repository.SecurityUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentServiceImplementation implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final SecurityUserRepository userRepository;
    private final IncidentMapper incidentMapper;
    private final GeometryFactory geometryFactory;
    private final GeocodingService geocodingService;

    @Override
    public IncidentResponseDTO getById(UUID id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id: " + id));
        return incidentMapper.toResponse(incident);
    }

    @Override
    public Page<IncidentResponseDTO> getAllIncidents(Pageable pageable) {
        return incidentRepository.findAll(pageable)
                .map(incidentMapper::toResponse);
    }

    @Override
    @Transactional
    public IncidentResponseDTO register(IncidentRequestDTO request) {
        SecurityUser user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    SecurityUser newUser = SecurityUser.builder()
                            .email(request.getEmail())
                            .phoneNumber(request.getPhoneNumber())
                            .role(UserRole.CITIZEN)
                            .authProvider(AuthProviderType.LOCAL)
                            .enabled(true)
                            .build();
                    return userRepository.save(newUser);
                });

        Incident incident = incidentMapper.toEntity(request, geometryFactory);
        incident.setUser(user);

        if (request.getLatitude() == null || request.getLongitude() == null) {
            String fullAddress = String.format("%s, %s, %s, %s, %s, %s",
                    request.getAddress(), request.getStreet(), request.getLandmark(),
                    request.getCity(), request.getDistrict(), request.getCountry());
            
            double[] coords = geocodingService.getCoordinates(fullAddress);
            incident.setLocation(geometryFactory.createPoint(new Coordinate(coords[0], coords[1])));
        }

        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @Override
    @Transactional
    public IncidentResponseDTO updatePublic(UUID id, @Valid IncidentRequestDTO request) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id: " + id));

        Point point;
        if (request.getLatitude() != null && request.getLongitude() != null) {
            point = geometryFactory.createPoint(new Coordinate(request.getLongitude(), request.getLatitude()));
        } else {
            String fullAddress = String.format("%s, %s, %s, %s, %s, %s",
                    request.getAddress(), request.getStreet(), request.getLandmark(),
                    request.getCity(), request.getDistrict(), request.getCountry());
            double[] coords = geocodingService.getCoordinates(fullAddress);
            point = geometryFactory.createPoint(new Coordinate(coords[0], coords[1]));
        }

        incident.setLocation(point);
        incident.setReporter(request.getReporter());
        incident.setDescription(request.getDescription());
        incident.setAddress(request.getAddress());
        incident.setStreet(request.getStreet());
        incident.setLandmark(request.getLandmark());
        incident.setCity(request.getCity());
        incident.setDistrict(request.getDistrict());
        incident.setCountry(request.getCountry());

        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @Override
    @Transactional
    public IncidentResponseDTO updateAdmin(UUID id, @Valid IncidentRequestDTO request) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id: " + id));

        incident.setType(IncidentType.valueOf(request.getType().name()));
        
        Point point;
        if (request.getLatitude() != null && request.getLongitude() != null) {
            point = geometryFactory.createPoint(new Coordinate(request.getLongitude(), request.getLatitude()));
        } else {
            String fullAddress = String.format("%s, %s, %s, %s, %s, %s",
                    request.getAddress(), request.getStreet(), request.getLandmark(),
                    request.getCity(), request.getDistrict(), request.getCountry());
            double[] coords = geocodingService.getCoordinates(fullAddress);
            point = geometryFactory.createPoint(new Coordinate(coords[0], coords[1]));
        }

        incident.setLocation(point);
        incident.setReporter(request.getReporter());
        incident.setDescription(request.getDescription());
        incident.setAddress(request.getAddress());
        incident.setStreet(request.getStreet());
        incident.setLandmark(request.getLandmark());
        incident.setCity(request.getCity());
        incident.setDistrict(request.getDistrict());
        incident.setCountry(request.getCountry());

        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @Override
    public void deleteIncident(UUID id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id: " + id));
        incidentRepository.delete(incident);
    }

    @Override
    public List<IncidentResponseDTO> getMyIncidents(UUID userId) {
        return incidentRepository.findByUserIdOrderByStatusDesc(userId)
                .stream()
                .map(incidentMapper::toResponse)
                .toList();

    }
}
