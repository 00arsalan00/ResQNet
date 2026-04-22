package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.VolunteerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VolunteerAssignmentRepository extends JpaRepository<VolunteerAssignment, UUID> {

    boolean existsByIncident_IdAndVolunteer_Id(UUID incidentId, UUID volunteerId);

}