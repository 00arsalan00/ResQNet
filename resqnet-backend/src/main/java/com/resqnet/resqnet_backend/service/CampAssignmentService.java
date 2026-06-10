package com.resqnet.resqnet_backend.service;

import java.util.UUID;

public interface CampAssignmentService {
    void assignPeople(UUID campId, int count);

    void releasePeople(UUID campId, int count);

    void assignCampToIncident(UUID incidentId, UUID campId);
}
