package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.ResourceAssignment;

import java.util.UUID;

public interface ResourceAssignmentService {

    ResourceAssignment assignResource(UUID resourceId, UUID campId, int quantity);

    void removeAssignment(UUID assignmentId);
}