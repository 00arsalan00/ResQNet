package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.ResourceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ResourceAssignmentRepository
        extends JpaRepository<ResourceAssignment, UUID> {

}