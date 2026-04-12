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
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService incidentService;

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> getIncident(@PathVariable UUID id){
        IncidentResponseDTO response = incidentService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<IncidentResponseDTO>> getAllIncidents(Pageable pageable){
        Page<IncidentResponseDTO> response = incidentService.getAllIncidents(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public  ResponseEntity<IncidentResponseDTO> registerIncident(@Valid @RequestBody IncidentRequestDTO request){
        IncidentResponseDTO response = incidentService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> updateIncident(@PathVariable UUID id, @Valid @RequestBody IncidentRequestDTO request){
        IncidentResponseDTO response = incidentService.update(id,request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> deleteIncident(@PathVariable UUID id){
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }



}
