package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.dto.ReliefCampRequestDTO;
import com.resqnet.resqnet_backend.dto.ReliefCampResponseDTO;
import com.resqnet.resqnet_backend.service.ReliefCampService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/camps")
@RequiredArgsConstructor
public class ReliefCampController {

    private final ReliefCampService campService;

    @PostMapping
    public ResponseEntity<ReliefCampResponseDTO> createCamp(
            @RequestBody ReliefCampRequestDTO request) {

        return ResponseEntity.status(201)
                .body(campService.createCamp(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllCamps(Pageable pageable) {
        return ResponseEntity.ok(campService.getAllCamps(pageable));
    }

    @GetMapping("/{campId}")
    public ResponseEntity<ReliefCampResponseDTO> getCampById(
            @PathVariable UUID campId) {

        return ResponseEntity.ok(campService.getCampById(campId));
    }

    @GetMapping("/{campId}/capacity")
    public ResponseEntity<Integer> getCapacity(@PathVariable UUID campId) {
        return ResponseEntity.ok(campService.getCapacity(campId));
    }

    @GetMapping("/{campId}/occupancy")
    public ResponseEntity<Integer> getOccupancy(@PathVariable UUID campId) {
        return ResponseEntity.ok(campService.getOccupancy(campId));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCamps(Pageable pageable) {
        return ResponseEntity.ok(campService.getAvailableCamps(pageable));
    }

    @PatchMapping("/{campId}/status")
    public ResponseEntity<ReliefCampResponseDTO> updateStatus(
            @PathVariable UUID campId,
            @RequestParam String status) {

        return ResponseEntity.ok(campService.updateStatus(campId, status));
    }

    @DeleteMapping("/{campId}")
    public ResponseEntity<Void> deleteCamp(@PathVariable UUID campId) {
        campService.deleteCamp(campId);
        return ResponseEntity.noContent().build();
    }
}
