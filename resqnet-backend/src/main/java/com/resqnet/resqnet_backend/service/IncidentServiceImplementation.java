package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.entity.Incident;
import com.resqnet.resqnet_backend.entity.IncidentType;
import com.resqnet.resqnet_backend.exception.IncidentNotFoundException;
import com.resqnet.resqnet_backend.mapper.IncidentMapper;
import com.resqnet.resqnet_backend.repository.IncidentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentServiceImplementation implements IncidentService {
    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;
    private final GeometryFactory geometryFactory;

    @Override
    public IncidentResponseDTO getById(UUID id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id: " + id));

        return incidentMapper.toResponse(incident);
    }

    @Override
    public Page<IncidentResponseDTO> getAllIncidents(Pageable pageable) {
        Page<Incident> page = incidentRepository.findAll(pageable);
        return page.map(incidentMapper::toResponse);
    }

    @Override
    public IncidentResponseDTO register(IncidentRequestDTO request) {
        Incident incident = incidentMapper.toEntity(request,geometryFactory);
        incidentRepository.save(incident);
        return incidentMapper.toResponse(incident);

    }

    @Override
    public IncidentResponseDTO update(UUID id, @Valid IncidentRequestDTO request) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id: " + id));

        incident.setType(IncidentType.valueOf(request.getType().name()));
        incident.setSeverity(request.getSeverity());
        incident.setLocation(
                geometryFactory.createPoint(
                        new Coordinate(request.getLongitude(), request.getLatitude())
                )
        );
        incident.setReporter(request.getReporter());
        Incident updated = incidentRepository.save(incident);
        return incidentMapper.toResponse(updated);
    }

    @Override
    public void deleteIncident(UUID id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id: " + id));

        incidentRepository.delete(incident);
    }
}
