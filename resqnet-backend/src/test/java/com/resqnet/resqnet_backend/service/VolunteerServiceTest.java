package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.VolunteerRequestDTO;
import com.resqnet.resqnet_backend.dto.VolunteerResponseDTO;
import com.resqnet.resqnet_backend.entity.Volunteer;
import com.resqnet.resqnet_backend.exception.VolunteerNotFoundException;
import com.resqnet.resqnet_backend.mapper.VolunteerMapper;
import com.resqnet.resqnet_backend.repository.VolunteerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private VolunteerMapper volunteerMapper;

    @Mock
    private GeometryFactory geometryFactory;

    @InjectMocks
    private VolunteerServiceImplementation volunteerService;

    @Test
    void testGetById_success() {
        UUID id = UUID.randomUUID();
        Volunteer volunteer = new Volunteer();
        VolunteerResponseDTO dto = VolunteerResponseDTO.builder().id(id).build();

        when(volunteerRepository.findById(id)).thenReturn(Optional.of(volunteer));
        when(volunteerMapper.toResponse(volunteer)).thenReturn(dto);

        VolunteerResponseDTO result = volunteerService.getById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void testGetById_notFound() {
        UUID id = UUID.randomUUID();

        when(volunteerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(VolunteerNotFoundException.class,
                () -> volunteerService.getById(id));
    }

    @Test
    void testRegisterVolunteer() {
        VolunteerRequestDTO request = new VolunteerRequestDTO();
        Volunteer volunteer = new Volunteer();
        Volunteer saved = new Volunteer();
        VolunteerResponseDTO response = VolunteerResponseDTO.builder().build();

        when(volunteerMapper.toEntity(request)).thenReturn(volunteer);
        when(volunteerRepository.save(volunteer)).thenReturn(saved);
        when(volunteerMapper.toResponse(saved)).thenReturn(response);

        VolunteerResponseDTO result = volunteerService.registerVolunteer(request);

        assertNotNull(result);
        verify(volunteerRepository).save(volunteer);
    }
}