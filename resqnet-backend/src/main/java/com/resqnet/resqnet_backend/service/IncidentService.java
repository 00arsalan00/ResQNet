package com.resqnet.resqnet_backend.service;


import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IncidentService {

    IncidentResponseDTO getById(UUID id);

    Page<IncidentResponseDTO> getAllIncidents(Pageable pageable);

    IncidentResponseDTO register(IncidentRequestDTO request);

    void deleteIncident(UUID id);

    IncidentResponseDTO updatePublic(UUID id, @Valid IncidentRequestDTO request);

    IncidentResponseDTO updateAdmin(UUID id, @Valid IncidentRequestDTO request);
}
