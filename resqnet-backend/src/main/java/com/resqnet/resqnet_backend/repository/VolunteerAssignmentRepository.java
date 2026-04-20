package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.VolunteerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VolunteerAssignmentRepository extends JpaRepository<VolunteerAssignment, UUID> {

    boolean existsByIncidentIdAndVolunteerId(UUID incidentId, UUID volunteerId);

    List<VolunteerAssignment> findByIncidentId(UUID incidentId);
}
