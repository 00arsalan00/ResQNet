package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.service.RescueTeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class RescueTeamController {
    private final RescueTeamService rescueTeamService;

    @GetMapping
    public ResponseEntity<?> getTeams(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) SkillType skill,
            Pageable pageable) {

        if (id != null) {
            return ResponseEntity.ok(rescueTeamService.getById(id));
        }

        if (name != null) {
            return ResponseEntity.ok(rescueTeamService.getByName(name));
        }

        if (skill != null) {
            return ResponseEntity.ok(rescueTeamService.getBySkill(skill));
        }

        return ResponseEntity.ok(rescueTeamService.getAllTeams(pageable));
    }

    @PostMapping
    public ResponseEntity<RescueTeamResponseDTO> registerTeam(@Valid @RequestBody RescueTeamRequestDTO request) {
        RescueTeamResponseDTO response = rescueTeamService.registerTeam(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RescueTeamResponseDTO> updateTeam(@PathVariable UUID id, @Valid @RequestBody RescueTeamRequestDTO request) {
        RescueTeamResponseDTO response = rescueTeamService.updateTeam(id,request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        rescueTeamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
