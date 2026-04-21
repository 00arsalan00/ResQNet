package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.*;
import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.ResourceNotFoundException;
import com.resqnet.resqnet_backend.mapper.ResourceMapper;
import com.resqnet.resqnet_backend.repository.ResourceRepository;
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
class ResourceServiceTest {

    @Mock
    private ResourceRepository repository;

    @Mock
    private GeometryFactory geometryFactory;

    @Mock
    private ResourceMapper mapper;

    @InjectMocks
    private ResourceServiceImplementation service;

    @Test
    void createResource_success() {

        ResourceRequestDTO request = new ResourceRequestDTO();
        request.setType(ResourceType.FOOD);
        request.setQuantity(100);
        request.setLatitude(10.0);
        request.setLongitude(20.0);

        Resource saved = new Resource();
        ResourceResponseDTO response = ResourceResponseDTO.builder().build();

        when(repository.save(any())).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        ResourceResponseDTO result = service.createResource(request);

        assertNotNull(result);
        verify(repository).save(any(Resource.class));
    }

    @Test
    void getById_notFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(id));
    }
}