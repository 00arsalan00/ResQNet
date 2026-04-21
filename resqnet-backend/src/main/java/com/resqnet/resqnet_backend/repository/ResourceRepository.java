package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.Resource;
import com.resqnet.resqnet_backend.entity.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    Page<Resource> findByType(ResourceType type, Pageable pageable);

    Page<Resource> findByAvailableQuantityGreaterThan(Integer quantity, Pageable pageable);
}