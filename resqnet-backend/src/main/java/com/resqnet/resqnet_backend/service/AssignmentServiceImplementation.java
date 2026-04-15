package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AssignmentServiceImplementation implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final IncidentRepository incidentRepository;
    private final RescueTeamRepository rescueTeamRepository;

    @Override
    @Transactional
    public IncidentAssignment assignTeam(UUID incidentId, UUID teamId){

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(()-> new IncidentNotFoundException("Incident not found"));

        RescueTeam rescueTeam = rescueTeamRepository.findById(teamId)
                .orElseThrow(()-> new RescueTeamNotFoundException("Team not found"));

        boolean alreadyAssigned = assignmentRepository
                .existsByIncidentIdAndRescueTeamId(incidentId, teamId);

        if (alreadyAssigned) {
            throw new TeamAlreadyAssignedToIncidentException("Team already assigned to incident");
        }

        long activeAssignments = assignmentRepository
                .countByRescueTeamIdAndStatusIn(
                        teamId,
                        List.of(AssignmentStatus.ASSIGNED, AssignmentStatus.IN_PROGRESS)
                );

        if (activeAssignments >= rescueTeam.getCapacity()) {
            throw new CapacityExceededException("Team capacity exceeded");
        }

        IncidentAssignment assignment = IncidentAssignment.builder()
                .incident(incident)
                .rescueTeam(rescueTeam)
                .status(AssignmentStatus.ASSIGNED)
                .build();

        return assignmentRepository.save(assignment);
    }


}
