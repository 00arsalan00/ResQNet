package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.*;
import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.ResourceNotFoundException;
import com.resqnet.resqnet_backend.mapper.ResourceMapper;
import com.resqnet.resqnet_backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceServiceImplementation implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final GeometryFactory geometryFactory;

    @Override
    public ResourceResponseDTO createResource(ResourceRequestDTO request) {

        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );

        Resource resource = Resource.builder()
                .type(request.getType())
                .totalQuantity(request.getQuantity())
                .availableQuantity(request.getQuantity())
                .warehouseLocation(point)
                .build();

        return resourceMapper.toResponse(resourceRepository.save(resource));
    }

    @Override
    public ResourceResponseDTO updateResource(UUID id, ResourceRequestDTO request) {

        Resource resource = getEntity(id);

        resource.setType(request.getType());
        resource.setTotalQuantity(request.getQuantity());

        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );
        resource.setWarehouseLocation(point);

        return resourceMapper.toResponse(resourceRepository.save(resource));
    }

    @Override
    public void deleteResource(UUID id) {
        resourceRepository.deleteById(id);
    }

    @Override
    public ResourceResponseDTO getById(UUID id) {
        return resourceMapper.toResponse(getEntity(id));
    }

    @Override
    public Page<ResourceResponseDTO> getAllResources(Pageable pageable) {
        return resourceRepository.findAll(pageable)
                .map(resourceMapper::toResponse);
    }

    @Override
    public Page<ResourceResponseDTO> getByType(ResourceType type, Pageable pageable) {
        return resourceRepository.findByType(type, pageable)
                .map(resourceMapper::toResponse);
    }

    private Resource getEntity(UUID id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }
}