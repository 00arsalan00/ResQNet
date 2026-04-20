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
@RequestMapping("/api")
@RequiredArgsConstructor
public class RescueTeamController {

    private final RescueTeamService rescueTeamService;

    @GetMapping("/teams")
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

    @PostMapping("/admin/teams")
    public ResponseEntity<RescueTeamResponseDTO> registerTeam(@Valid @RequestBody RescueTeamRequestDTO request) {
        return ResponseEntity.status(201).body(rescueTeamService.registerTeam(request));
    }

    @PutMapping("/admin/teams/{id}")
    public ResponseEntity<RescueTeamResponseDTO> updateTeam(@PathVariable UUID id,
                                                            @Valid @RequestBody RescueTeamRequestDTO request) {
        return ResponseEntity.ok(rescueTeamService.updateTeam(id, request));
    }

    @DeleteMapping("/admin/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        rescueTeamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}