package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.dto.IncidentResponseDTO;
import com.resqnet.resqnet_backend.entity.Incident;
import com.resqnet.resqnet_backend.entity.IncidentType;
import com.resqnet.resqnet_backend.mapper.IncidentMapper;
import com.resqnet.resqnet_backend.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceImplementationTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private IncidentMapper incidentMapper;

    @Mock
    private GeometryFactory geometryFactory;

    @Mock
    private GeocodingService geocodingService;

    @InjectMocks
    private IncidentServiceImplementation incidentService;

    @Test
    void shouldRegisterIncident() {
        IncidentRequestDTO request = new IncidentRequestDTO();
        request.setType(IncidentType.FLOOD);
        request.setLatitude(28.61);
        request.setLongitude(77.20);
        request.setReporter("Arsalan");
        request.setDescription("Test");
        request.setCity("Delhi");
        request.setDistrict("Central");
        request.setCountry("India");

        Incident incident = new Incident();
        Incident savedIncident = new Incident();
        IncidentResponseDTO response = IncidentResponseDTO.builder().build();

        when(incidentMapper.toEntity(request, geometryFactory)).thenReturn(incident);
        when(incidentRepository.save(incident)).thenReturn(savedIncident);
        when(incidentMapper.toResponse(savedIncident)).thenReturn(response);

        IncidentResponseDTO result = incidentService.register(request);

        assertNotNull(result);
        verify(incidentRepository, times(1)).save(incident);
    }
}
