package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.IncidentAssignment;

import java.util.UUID;

public interface AssignmentService {

    IncidentAssignment assignTeam(UUID incidentId, UUID teamId);

}
