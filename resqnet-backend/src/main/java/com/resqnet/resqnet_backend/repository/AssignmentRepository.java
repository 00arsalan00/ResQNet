package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.AssignmentStatus;
import com.resqnet.resqnet_backend.entity.Incident;
import com.resqnet.resqnet_backend.entity.IncidentAssignment;
import com.resqnet.resqnet_backend.entity.RescueTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<IncidentAssignment, UUID> {

    boolean existsByIncidentIdAndRescueTeamId(UUID incidentId, UUID teamId);

    long countByRescueTeamIdAndStatusIn(UUID teamId, List<AssignmentStatus> statuses);

    Page<IncidentAssignment> findAllByIncidentId(UUID incidentId, Pageable pageable);

    Page<IncidentAssignment> findAllByRescueTeamId(UUID teamId, Pageable pageable);
}
