package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.dto.ResourceRequestDTO;
import com.resqnet.resqnet_backend.dto.ResourceResponseDTO;
import com.resqnet.resqnet_backend.entity.ResourceType;
import com.resqnet.resqnet_backend.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<?> getResources(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) ResourceType type,
            Pageable pageable) {

        if (id != null) {
            return ResponseEntity.ok(resourceService.getById(id));
        }

        if (type != null) {
            return ResponseEntity.ok(resourceService.getByType(type, pageable));
        }

        return ResponseEntity.ok(resourceService.getAllResources(pageable));
    }

    @PostMapping
    public ResponseEntity<ResourceResponseDTO> createResource(
            @Valid @RequestBody ResourceRequestDTO request) {

        return ResponseEntity.status(201)
                .body(resourceService.createResource(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> updateResource(
            @PathVariable UUID id,
            @Valid @RequestBody ResourceRequestDTO request) {

        return ResponseEntity.ok(resourceService.updateResource(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable UUID id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}