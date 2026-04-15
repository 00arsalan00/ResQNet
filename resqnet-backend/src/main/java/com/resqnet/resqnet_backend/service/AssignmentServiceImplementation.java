package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.AssignmentRequestDTO;
import com.resqnet.resqnet_backend.dto.AssignmentResponseDTO;
import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.mapper.AssignmentMapper;
import com.resqnet.resqnet_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AssignmentServiceImplementation implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final IncidentRepository incidentRepository;
    private final RescueTeamRepository rescueTeamRepository;
    private final AssignmentMapper assignmentMapper;

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

    @Override
    public Page<AssignmentResponseDTO> getAllAssignments(Pageable pageable){
        Page<IncidentAssignment> assignments = assignmentRepository.findAll(pageable);

        if (assignments.isEmpty()) {
            throw new AssignmentNotFound("No assignments found");
        }

        return assignments.map(assignmentMapper::toResponse);
    }

    @Override
    public AssignmentResponseDTO getAssignmentById(UUID assignmentId) {

        IncidentAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFound(
                        "Assignment not found with id: " + assignmentId
                ));

        return assignmentMapper.toResponse(assignment);
    }

    @Override
    public Page<AssignmentResponseDTO> getAssignmentsByIncidentId(UUID incidentId, Pageable pageable){
        Page<IncidentAssignment> assignments =
                assignmentRepository.findAllByIncidentId(incidentId, pageable);

        if (assignments.isEmpty()) {
            throw new AssignmentNotFound("No assignments for incident: " + incidentId);
        }

        return assignments.map(assignmentMapper::toResponse);
    }

    @Override
    public Page<AssignmentResponseDTO> getAssignmentsByTeamId(UUID teamId, Pageable pageable){
        Page<IncidentAssignment> assignments =
                assignmentRepository.findAllByRescueTeamId(teamId, pageable);

        if (assignments.isEmpty()) {
            throw new AssignmentNotFound("No assignments for team: " + teamId);
        }

        return assignments.map(assignmentMapper::toResponse);
    }

    @Override
    public AssignmentResponseDTO updateAssignment(UUID assignmentId, AssignmentRequestDTO request){

        IncidentAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFound("Assignment not found: " + assignmentId));

        Incident incident = incidentRepository.findById(request.getIncidentId())
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found"));

        RescueTeam team = rescueTeamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new RescueTeamNotFoundException("Team not found"));

        assignment.setIncident(incident);
        assignment.setRescueTeam(team);
        assignment.setStatus(request.getStatus());

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public void deleteAssignment(UUID assignmentId){
        IncidentAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFound("Assignment not found: " + assignmentId));

        assignmentRepository.delete(assignment);
    }



}
