package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.VolunteerAssignment;

import java.util.UUID;

public interface VolunteerAssignmentService {
    public VolunteerAssignment assignVolunteer(UUID incidentId, UUID volunteerId);

    public void removeAssignment(UUID assignmentId);
}
