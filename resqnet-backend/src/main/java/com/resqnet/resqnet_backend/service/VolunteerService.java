package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.dto.VolunteerRequestDTO;
import com.resqnet.resqnet_backend.dto.VolunteerResponseDTO;
import com.resqnet.resqnet_backend.entity.IncidentAssignment;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.Volunteer;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VolunteerService {

    VolunteerResponseDTO getById(UUID volunteerId);

    Page<VolunteerResponseDTO> getByName(String name, Pageable pageable);

    Page<VolunteerResponseDTO> getBySkill(SkillType skill, Pageable pageable);

    Page<VolunteerResponseDTO> getAllVolunteers(Pageable pageable);

    void deleteVolunteer(UUID volunteerId);

    VolunteerResponseDTO registerVolunteer(VolunteerRequestDTO request);

    VolunteerResponseDTO updateVolunteer(UUID volunteerId, VolunteerRequestDTO request);
}