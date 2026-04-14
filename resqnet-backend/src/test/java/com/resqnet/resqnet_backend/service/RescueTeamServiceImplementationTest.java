package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamRequestDTO;
import com.resqnet.resqnet_backend.dto.RescueTeamResponseDTO;
import com.resqnet.resqnet_backend.entity.RescueTeam;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.TeamStatus;
import com.resqnet.resqnet_backend.exception.RescueTeamNotFoundException;
import com.resqnet.resqnet_backend.mapper.RescueTeamMapper;
import com.resqnet.resqnet_backend.repository.RescueTeamRepository;
import org.hibernate.query.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Rescue Team Test")
class RescueTeamServiceImplementationTest {
    @Mock
    private  RescueTeamRepository rescueTeamRepository;
    @Mock
    private  RescueTeamMapper rescueTeamMapper;
    @Mock
    private  GeometryFactory geometryFactory;
    @InjectMocks
    private RescueTeamServiceImplementation rescueTeamService;

    private RescueTeam rescueTeam;
    private RescueTeamResponseDTO  response;
    private RescueTeamRequestDTO request;

    @Test
    void createRescueTeam(){

        RescueTeamRequestDTO requestDTO = new RescueTeamRequestDTO();
        requestDTO.setTeamName("XYZ_Rescue_Team");
        requestDTO.setCapacity(5);
        requestDTO.setCaptainName("Vivek");
        requestDTO.setContactInfo("xyz@helper.com");
        requestDTO.setSkills(List.of(SkillType.RESCUE,SkillType.MEDICAL));
        requestDTO.setLatitude(28.61);
        requestDTO.setLongitude(77.20);

        RescueTeam rescueTeam = new RescueTeam();
        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamMapper.toEntity(any(RescueTeamRequestDTO.class)))
                .thenReturn(rescueTeam);

        when(rescueTeamRepository.save(any()))
                .thenReturn(rescueTeam);

        when(rescueTeamMapper.toResponse(any()))
                .thenReturn(response);

        RescueTeamResponseDTO result = rescueTeamService.registerTeam(requestDTO);

        assertNotNull(result);
        verify(rescueTeamRepository,times(1)).save(rescueTeam);

    }

    @Test
    void getRescueTeamById(){
        UUID id = UUID.randomUUID();

        RescueTeam rescueTeam = new RescueTeam();
        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.of(rescueTeam));
        when(rescueTeamMapper.toResponse(any())).thenReturn(response);

        RescueTeamResponseDTO result = rescueTeamService.getById(id);

        assertNotNull(result);
        verify(rescueTeamRepository,times(1)).findById(id);

    }

    @Test
    void getRescueTeamBySkill(){
        SkillType skill =  SkillType.RESCUE;
        RescueTeam rescueTeam = new RescueTeam();
        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamRepository.findBySkill(skill)).thenReturn(Optional.of(rescueTeam));
        when(rescueTeamMapper.toResponse(any())).thenReturn(response);

        RescueTeamResponseDTO result = rescueTeamService.getBySkill(skill);
        assertNotNull(result);
        verify(rescueTeamRepository,times(1)).findBySkill(skill);
    }

    @Test
    void getRescueTeamByIdNotFound(){
        UUID id = UUID.randomUUID();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RescueTeamNotFoundException.class, () -> rescueTeamService.getById(id));
        verify(rescueTeamRepository,times(1)).findById(id);

    }

    @Test
    void updateRescueTeam(){
        RescueTeam existingRescueTeam = new RescueTeam();
        UUID id = UUID.randomUUID();
        RescueTeamRequestDTO request = new RescueTeamRequestDTO();
        request.setTeamName("XYZ_Rescue_Team");
        request.setCapacity(5);
        request.setCaptainName("Vivek");
        request.setContactInfo("xyz@helper.com");
        request.setSkills(List.of(SkillType.RESCUE,SkillType.MEDICAL));
        request.setLatitude(28.61);
        request.setLongitude(77.20);

        RescueTeamResponseDTO response = RescueTeamResponseDTO.builder().build();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.of(existingRescueTeam));
        when(rescueTeamRepository.save(any())).thenReturn(existingRescueTeam);
        when(rescueTeamMapper.toResponse(any())).thenReturn(response);

        RescueTeamResponseDTO result = rescueTeamService.updateTeam(id, request);
        assertNotNull(result);
        verify(rescueTeamRepository,times(1)).findById(id);
        verify(rescueTeamRepository,times(1)).save(any());


    }

    @Test
    void deleteRescueTeamById(){
        UUID id = UUID.randomUUID();
        RescueTeam existingRescueTeam = new RescueTeam();

        when(rescueTeamRepository.findById(id)).thenReturn(Optional.of(existingRescueTeam));
        rescueTeamService.deleteTeam(id);
        verify(rescueTeamRepository,times(1)).findById(id);
        verify(rescueTeamRepository,times(1)).delete(existingRescueTeam);


    }


}




