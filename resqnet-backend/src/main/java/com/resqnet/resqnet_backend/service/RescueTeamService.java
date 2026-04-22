package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.entity.SkillType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RescueTeamService {

    RescueTeamResponseDTO getById(UUID id);

    Page<RescueTeamResponseDTO> getAllTeams(Pageable pageable);

    RescueTeamResponseDTO getByName(String name);

    List<RescueTeamResponseDTO> getBySkill(SkillType skill);

    RescueTeamResponseDTO registerTeam(RescueTeamRequestDTO request);

    RescueTeamResponseDTO updateTeam(UUID id, RescueTeamRequestDTO request);

    void deleteTeam(UUID id);
}
