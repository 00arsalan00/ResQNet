package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.service.CampAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/camp-assignments")
@RequiredArgsConstructor
public class CampAssignmentController {

    private final CampAssignmentService campAssignmentService;

    @PostMapping("/{campId}/assign")
    public ResponseEntity<?> assignPeople(
            @PathVariable UUID campId,
            @RequestParam int count) {

        campAssignmentService.assignPeople(campId, count);
        return ResponseEntity.ok("Assigned successfully");
    }

    @PostMapping("/{campId}/release")
    public ResponseEntity<?> releasePeople(
            @PathVariable UUID campId,
            @RequestParam int count) {

        campAssignmentService.releasePeople(campId, count);
        return ResponseEntity.ok("Released successfully");
    }
}