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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private ReliefCamp reliefCamp;

    // 🔹 Quantity allocated
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