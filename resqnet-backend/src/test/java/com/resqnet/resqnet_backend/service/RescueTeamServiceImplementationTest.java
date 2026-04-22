package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.entity.RescueTeam;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.exception.RescueTeamNotFoundException;
import com.resqnet.resqnet_backend.mapper.RescueTeamMapper;
import com.resqnet.resqnet_backend.repository.RescueTeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Rescue Team Test")
class RescueTeamServiceImplementationTest {

    @Mock
    private RescueTeamRepository rescueTeamRepository;

    @Mock
    private RescueTeamMapper rescueTeamMapper;

    @Mock
    private GeometryFactory geometryFactory;

    @InjectMocks
    private RescueTeamServiceImplementation rescueTeamService;

    @Test
    void createRescueTeam() {

        RescueTeamRequestDTO requestDTO = new RescueTeamRequestDTO();
        requestDTO.setTeamName("XYZ_Rescue_Team");
        requestDTO.setCapacity(5);
        requestDTO.setCaptainName("abc");
        requestDTO.setContactInfo("xyz@helper.com");
        requestDTO.setSkills(List.of(SkillType.RESCUE, SkillType.MEDICAL));
        requestDTO.setLatitude(28.61);
        requestDTO.setLongitude(77.20);

        RescueTeam rescueTeam = new RescueTeam();
        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamMapper.toEntity(any())).thenReturn(rescueTeam);
        when(rescueTeamRepository.save(any())).thenReturn(rescueTeam);
        when(rescueTeamMapper.toResponse(any())).thenReturn(response);

        RescueTeamResponseDTO result = rescueTeamService.registerTeam(requestDTO);

        assertNotNull(result);
        verify(rescueTeamRepository).save(rescueTeam);
    }

    @Test
    void getRescueTeamById() {
        UUID id = UUID.randomUUID();

        RescueTeam rescueTeam = new RescueTeam();
        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.of(rescueTeam));
        when(rescueTeamMapper.toResponse(any())).thenReturn(response);

        RescueTeamResponseDTO result = rescueTeamService.getById(id);

        assertNotNull(result);
        verify(rescueTeamRepository).findById(id);
    }

    @Test
    void getRescueTeamBySkill() {
        SkillType skill = SkillType.RESCUE;

        RescueTeam team1 = new RescueTeam();
        RescueTeam team2 = new RescueTeam();

        List<RescueTeam> teams = List.of(team1, team2);

        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamRepository.findBySkillsContaining(skill))
                .thenReturn(teams);

        when(rescueTeamMapper.toResponse(any()))
                .thenReturn(response);

        List<RescueTeamResponseDTO> result = rescueTeamService.getBySkill(skill);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(rescueTeamRepository).findBySkillsContaining(skill);
    }

    @Test
    void getRescueTeamBySkill_notFound() {
        SkillType skill = SkillType.RESCUE;

        when(rescueTeamRepository.findBySkillsContaining(skill))
                .thenReturn(List.of());

        assertThrows(RescueTeamNotFoundException.class,
                () -> rescueTeamService.getBySkill(skill));
    }

    @Test
    void getRescueTeamByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rescueTeamService.getById(id));
    }

    @Test
    void updateRescueTeam() {
        UUID id = UUID.randomUUID();

        RescueTeam existing = new RescueTeam();

        RescueTeamRequestDTO request = new RescueTeamRequestDTO();
        request.setTeamName("XYZ");
        request.setCapacity(5);
        request.setCaptainName("Vivek");
        request.setContactInfo("xyz@helper.com");
        request.setSkills(List.of(SkillType.RESCUE));
        request.setLatitude(28.61);
        request.setLongitude(77.20);

        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.of(existing));
        when(rescueTeamRepository.save(any())).thenReturn(existing);
        when(rescueTeamMapper.toResponse(any())).thenReturn(response);

        RescueTeamResponseDTO result = rescueTeamService.updateTeam(id, request);

        assertNotNull(result);
        verify(rescueTeamRepository).save(any());
    }

    @Test
    void deleteRescueTeamById() {
        UUID id = UUID.randomUUID();
        RescueTeam existing = new RescueTeam();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.of(existing));

        rescueTeamService.deleteTeam(id);

        verify(rescueTeamRepository).delete(existing);
    }
}