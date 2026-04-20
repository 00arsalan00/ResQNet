package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.*;
import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.CampNotFoundException;
import com.resqnet.resqnet_backend.mapper.ReliefCampMapper;
import com.resqnet.resqnet_backend.repository.ReliefCampRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReliefCampServiceTest {

    @Mock
    private ReliefCampRepository repository;

    @Mock
    private ReliefCampMapper mapper;

    @Mock
    private GeometryFactory geometryFactory;

    @InjectMocks
    private ReliefCampServiceImplementation service;

    @Test
    void createCamp_success() {
        ReliefCampRequestDTO request = new ReliefCampRequestDTO();
        request.setName("Camp A");
        request.setCapacity(100);
        request.setLatitude(10.0);
        request.setLongitude(20.0);

        ReliefCamp camp = new ReliefCamp();
        ReliefCamp saved = new ReliefCamp();
        ReliefCampResponseDTO response = ReliefCampResponseDTO.builder().name("Camp A").build();

        when(repository.save(any())).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        ReliefCampResponseDTO result = service.createCamp(request);

        assertEquals("Camp A", result.getName());
        verify(repository).save(any());
    }

    @Test
    void getCampById_success() {
        UUID id = UUID.randomUUID();
        ReliefCamp camp = new ReliefCamp();
        ReliefCampResponseDTO dto = ReliefCampResponseDTO.builder().id(id).build();

        when(repository.findById(id)).thenReturn(Optional.of(camp));
        when(mapper.toResponse(camp)).thenReturn(dto);

        ReliefCampResponseDTO result = service.getCampById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void getCampById_notFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CampNotFoundException.class,
                () -> service.getCampById(id));
    }

    @Test
    void getAvailableCamps_success() {
        Pageable pageable = PageRequest.of(0, 10);

        ReliefCamp camp = new ReliefCamp();
        ReliefCampResponseDTO dto = ReliefCampResponseDTO.builder().name("Camp").build();

        Page<ReliefCamp> page = new PageImpl<>(List.of(camp));

        when(repository.findAvailableCamps(pageable)).thenReturn(page);
        when(mapper.toResponse(camp)).thenReturn(dto);

        Page<ReliefCampResponseDTO> result = service.getAvailableCamps(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deleteCamp_success() {
        UUID id = UUID.randomUUID();

        service.deleteCamp(id);

        verify(repository).deleteById(id);
    }
}