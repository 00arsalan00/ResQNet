package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceAssignmentServiceImplementation implements ResourceAssignmentService {

    private final ResourceRepository resourceRepository;
    private final ReliefCampRepository campRepository;
    private final ResourceAssignmentRepository assignmentRepository;

    @Override
    @Transactional
    public ResourceAssignment assignResource(UUID resourceId, UUID campId, int quantity) {

        if (quantity <= 0) {
            throw new InvalidOperationException("Quantity must be greater than 0");
        }

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        ReliefCamp camp = campRepository.findById(campId)
                .orElseThrow(() -> new CampNotFoundException("Camp not found"));

        if (resource.getAvailableQuantity() < quantity) {
            throw new ResourceUnavailableException("Not enough stock available");
        }

        resource.setAvailableQuantity(resource.getAvailableQuantity() - quantity);

        ResourceAssignment assignment = ResourceAssignment.builder()
                .resource(resource)
                .reliefCamp(camp)
                .quantity(quantity)
                .status(ResourceAssignmentStatus.ASSIGNED)
                .build();

        return assignmentRepository.save(assignment);
    }

    @Override
    @Transactional
    public void removeAssignment(UUID assignmentId) {

        ResourceAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFound("Assignment not found"));

        Resource resource = assignment.getResource();

        resource.setAvailableQuantity(
                resource.getAvailableQuantity() + assignment.getQuantity()
        );

        assignmentRepository.delete(assignment);
    }
}