package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VolunteerAssignmentServiceImplementation implements VolunteerAssignmentService {

    private final VolunteerAssignmentRepository volunteerAssignmentRepository;
    private final IncidentRepository incidentRepository;
    private final VolunteerRepository volunteerRepository;

    @Override
    public VolunteerAssignment assignVolunteer(UUID incidentId, UUID volunteerId){

        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found"));

        if (volunteer.getStatus() != VolunteerStatus.AVAILABLE) {
            throw new InvalidStatusTransitionException("Volunteer is not available");
        }

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found"));

        boolean exists = volunteerAssignmentRepository
                .existsByIncidentIdAndVolunteerId(incidentId, volunteerId);

        if (exists) {
            throw new VolunteerAlreadyAssignedToIncidentException(
                    "Volunteer already assigned to this incident");
        }

        VolunteerAssignment assignment = VolunteerAssignment.builder()
                .incident(incident)
                .volunteer(volunteer)
                .status(AssignmentStatus.ASSIGNED)
                .assignedAt(LocalDateTime.now())
                .build();

        volunteer.setStatus(VolunteerStatus.ASSIGNED);

        return volunteerAssignmentRepository.save(assignment);
    }

    @Override
    public void removeAssignment(UUID assignmentId) {

        VolunteerAssignment assignment = volunteerAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFound("Assignment not found"));

        Volunteer volunteer = assignment.getVolunteer();
        volunteer.setStatus(VolunteerStatus.AVAILABLE);

        volunteerAssignmentRepository.delete(assignment);
    }
}