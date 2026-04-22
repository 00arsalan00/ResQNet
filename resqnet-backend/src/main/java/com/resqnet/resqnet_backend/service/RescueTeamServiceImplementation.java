package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.entity.RescueTeam;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.exception.RescueTeamNotFoundException;
import com.resqnet.resqnet_backend.mapper.RescueTeamMapper;
import com.resqnet.resqnet_backend.repository.RescueTeamRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RescueTeamServiceImplementation implements RescueTeamService {

    private final RescueTeamRepository rescueTeamRepository;
    private final GeometryFactory geometryFactory;
    private final RescueTeamMapper rescueTeamMapper;

    @Override
    public RescueTeamResponseDTO getById(UUID id) {
        RescueTeam rescueTeam = rescueTeamRepository.findById(id)
                .orElseThrow(()-> new RescueTeamNotFoundException("Rescue Team Not Found"));

        return rescueTeamMapper.toResponse(rescueTeam);
    }

    @Override
    public Page<RescueTeamResponseDTO> getAllTeams(Pageable pageable) {
        Page<RescueTeam> rescueTeams = rescueTeamRepository.findAll(pageable);
        return rescueTeams.map(rescueTeamMapper::toResponse);
    }

    @Override
    public RescueTeamResponseDTO getByName(String name) {

        RescueTeam rescueTeam = rescueTeamRepository.findByTeamName(name)
                .orElseThrow(() -> new RescueTeamNotFoundException("Rescue Team Not Found"));

        return rescueTeamMapper.toResponse(rescueTeam);
    }

    @Override
    public List<RescueTeamResponseDTO> getBySkill(SkillType skill) {

        List<RescueTeam> teams = rescueTeamRepository.findBySkillsContaining(skill);

        if (teams.isEmpty()) {
            throw new RescueTeamNotFoundException("No Rescue Team found for skill: " + skill);
        }

        return teams.stream()
                .map(rescueTeamMapper::toResponse)
                .toList();
    }

    @Override
    public RescueTeamResponseDTO registerTeam(RescueTeamRequestDTO request){
        RescueTeam rescueTeam = rescueTeamMapper.toEntity(request);
        rescueTeamRepository.save(rescueTeam);
        return rescueTeamMapper.toResponse(rescueTeam);
    }

    @Override
    public RescueTeamResponseDTO updateTeam(UUID id, RescueTeamRequestDTO request) {
        RescueTeam rescueTeam = rescueTeamRepository.findById(id)
                .orElseThrow(()-> new RescueTeamNotFoundException("Rescue Team Not Found"));

        rescueTeam.setTeamName(request.getTeamName());
        rescueTeam.setCaptainName(request.getCaptainName());
        rescueTeam.setContactInfo(request.getContactInfo());
        rescueTeam.setCapacity(request.getCapacity());
        rescueTeam.setLocation(
                geometryFactory.createPoint(
                        new Coordinate(request.getLongitude(), request.getLatitude())
                )
        );
        rescueTeam.setSkills(request.getSkills());

        RescueTeam updated =  rescueTeamRepository.save(rescueTeam);
        return rescueTeamMapper.toResponse(updated);
    }

    @Override
    public void deleteTeam(UUID id) {
        RescueTeam rescueTeam = rescueTeamRepository.findById(id)
                .orElseThrow(()-> new RescueTeamNotFoundException("Rescue Team Not Found"));

        rescueTeamRepository.delete(rescueTeam);
    }


}
