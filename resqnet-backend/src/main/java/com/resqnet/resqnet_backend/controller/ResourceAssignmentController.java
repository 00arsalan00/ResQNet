package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.entity.ResourceAssignment;
import com.resqnet.resqnet_backend.service.ResourceAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/resource-assignments")
@RequiredArgsConstructor
public class ResourceAssignmentController {

    private final ResourceAssignmentService resourceAssignmentService;

    @PostMapping("/resources/{resourceId}/camps/{campId}")
    public ResponseEntity<ResourceAssignment> assignResource(
            @PathVariable UUID resourceId,
            @PathVariable UUID campId,
            @RequestParam int quantity) {

        ResourceAssignment assignment =
                resourceAssignmentService.assignResource(resourceId, campId, quantity);

        return ResponseEntity.status(201).body(assignment);
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> removeAssignment(@PathVariable UUID assignmentId) {

        resourceAssignmentService.removeAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}