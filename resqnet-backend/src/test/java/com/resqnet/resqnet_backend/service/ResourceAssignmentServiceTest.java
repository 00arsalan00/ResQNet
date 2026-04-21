package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceAssignmentServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ReliefCampRepository campRepository;

    @Mock
    private ResourceAssignmentRepository assignmentRepository;

    @InjectMocks
    private ResourceAssignmentServiceImplementation service;

    @Test
    void assignResource_success() {
        UUID resourceId = UUID.randomUUID();
        UUID campId = UUID.randomUUID();

        Resource resource = new Resource();
        resource.setAvailableQuantity(100);

        ReliefCamp camp = new ReliefCamp();

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(campRepository.findById(campId)).thenReturn(Optional.of(camp));
        when(assignmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ResourceAssignment result = service.assignResource(resourceId, campId, 50);

        assertEquals(50, result.getQuantity());
        assertEquals(50, resource.getAvailableQuantity());
    }

    @Test
    void assignResource_insufficientStock() {
        UUID resourceId = UUID.randomUUID();
        UUID campId = UUID.randomUUID();

        Resource resource = new Resource();
        resource.setAvailableQuantity(10);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(campRepository.findById(campId)).thenReturn(Optional.of(new ReliefCamp()));

        assertThrows(ResourceUnavailableException.class,
                () -> service.assignResource(resourceId, campId, 20));
    }

    @Test
    void removeAssignment_success() {
        UUID assignmentId = UUID.randomUUID();

        Resource resource = new Resource();
        resource.setAvailableQuantity(50);

        ResourceAssignment assignment = new ResourceAssignment();
        assignment.setResource(resource);
        assignment.setQuantity(20);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        service.removeAssignment(assignmentId);

        assertEquals(70, resource.getAvailableQuantity());
        verify(assignmentRepository).delete(assignment);
    }
}