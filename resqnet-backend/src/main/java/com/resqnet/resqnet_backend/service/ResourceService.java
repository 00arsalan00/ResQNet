package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.ResourceRequestDTO;
import com.resqnet.resqnet_backend.dto.ResourceResponseDTO;
import com.resqnet.resqnet_backend.entity.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ResourceService {

    ResourceResponseDTO createResource(ResourceRequestDTO request);

    ResourceResponseDTO updateResource(UUID id, ResourceRequestDTO request);

    void deleteResource(UUID id);

    ResourceResponseDTO getById(UUID id);

    Page<ResourceResponseDTO> getAllResources(Pageable pageable);

    Page<ResourceResponseDTO> getByType(ResourceType type, Pageable pageable);
}