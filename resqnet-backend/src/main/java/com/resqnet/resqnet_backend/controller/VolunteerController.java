package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.dto.VolunteerRequestDTO;
import com.resqnet.resqnet_backend.dto.VolunteerResponseDTO;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.service.VolunteerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/volunteers")
@RequiredArgsConstructor
public class VolunteerController {

    private final VolunteerService volunteerService;

    @GetMapping
    public ResponseEntity<?> getVolunteers(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) SkillType skill,
            Pageable pageable) {

        if (id != null) {
            return ResponseEntity.ok(volunteerService.getById(id));
        }

        if (name != null) {
            return ResponseEntity.ok(volunteerService.getByName(name,pageable));
        }

        if (skill != null) {
            return ResponseEntity.ok(volunteerService.getBySkill(skill,pageable));
        }

        return ResponseEntity.ok(volunteerService.getAllVolunteers(pageable));
    }

    @PostMapping
    public ResponseEntity<VolunteerResponseDTO> registerVolunteer(
            @Valid @RequestBody VolunteerRequestDTO request) {

        return ResponseEntity.status(201)
                .body(volunteerService.registerVolunteer(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VolunteerResponseDTO> updateVolunteer(
            @PathVariable UUID id,
            @Valid @RequestBody VolunteerRequestDTO request) {

        return ResponseEntity.ok(volunteerService.updateVolunteer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable UUID id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.noContent().build();
    }
}