package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.entity.VolunteerAssignment;
import com.resqnet.resqnet_backend.service.VolunteerAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assignments/volunteers")
@RequiredArgsConstructor
public class VolunteerAssignmentController {

    private final VolunteerAssignmentService volunteerAssignmentService;

    @PostMapping("/incidents/{incidentId}/volunteers/{volunteerId}")
    public ResponseEntity<VolunteerAssignment> assignVolunteer(
            @PathVariable UUID incidentId,
            @PathVariable UUID volunteerId) {

        VolunteerAssignment assignment =
                volunteerAssignmentService.assignVolunteer(incidentId, volunteerId);

        return ResponseEntity.status(201).body(assignment);
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> removeAssignment(@PathVariable UUID assignmentId) {
        volunteerAssignmentService.removeAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}