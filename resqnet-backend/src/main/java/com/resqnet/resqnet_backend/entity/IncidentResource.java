package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "incident_resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentResource {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    /**
     * Optional reference to the relief camp where this resource is allocated.
     * 
     * Semantics:
     * - If null: Resource is allocated at the incident level (strategic/general allocation).
     *   Used for resources not yet committed to a specific camp, or those managed
     *   incident-wide across all camps.
     * 
     * - If not null: Resource is tied to a specific relief camp (tactical/camp-specific allocation).
     *   This represents a concrete assignment of resources to a particular camp's operations.
     * 
     * This design supports both incident-wide and camp-specific resource tracking
     * within the same allocation framework.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private ReliefCamp reliefCamp;

    @Column(nullable = false)
    private Integer quantityAllocated;

    @Column(nullable = false)
    private Integer quantityUsed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentResourceStatus status;

    private LocalDateTime allocatedAt;
    private LocalDateTime usedAt;

    @PrePersist
    void onCreate() {
        this.allocatedAt = LocalDateTime.now();
        if (this.quantityUsed == null) this.quantityUsed = 0;
    }
}
