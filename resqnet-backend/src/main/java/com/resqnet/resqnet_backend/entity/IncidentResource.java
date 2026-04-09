package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "incident_resources",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"incident_id", "resource_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentResource {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Integer quantityUsed;

    @Column(nullable = false)
    private LocalDateTime allocatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;
}