package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping("/incidents/{id}")
    public ResponseEntity<IncidentResponseDTO> getIncident(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentService.getById(id));
    }

    @GetMapping("/incidents")
    public ResponseEntity<Page<IncidentResponseDTO>> getAllIncidents(Pageable pageable) {
        return ResponseEntity.ok(incidentService.getAllIncidents(pageable));
    }

    @PostMapping("/incidents")
    public ResponseEntity<IncidentResponseDTO> registerIncident(@Valid @RequestBody IncidentRequestDTO request) {
        return ResponseEntity.status(201).body(incidentService.register(request));
    }

    @PutMapping("/incidents/{id}")
    public ResponseEntity<IncidentResponseDTO> updateIncident(@PathVariable UUID id,
                                                              @Valid @RequestBody IncidentRequestDTO request) {
        return ResponseEntity.ok(incidentService.updatePublic(id, request));
    }

    @GetMapping("/admin/incidents")
    public ResponseEntity<Page<IncidentResponseDTO>> getAllIncidentsAdmin(Pageable pageable) {
        return ResponseEntity.ok(incidentService.getAllIncidents(pageable));
    }

    @PutMapping("/admin/incidents/{id}")
    public ResponseEntity<IncidentResponseDTO> updateIncidentAdmin(@PathVariable UUID id,
                                                                   @Valid @RequestBody IncidentRequestDTO request) {
        return ResponseEntity.ok(incidentService.updateAdmin(id, request));
    }

    @DeleteMapping("/admin/incidents/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable UUID id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
}