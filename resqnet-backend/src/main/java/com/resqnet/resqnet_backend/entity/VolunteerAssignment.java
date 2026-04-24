package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"incident_id", "volunteer_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VolunteerAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id",nullable=false)
    private Incident incident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id",nullable=false)
    private Volunteer volunteer;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    @Enumerated(EnumType.STRING)
    private AssignmentRole role;

    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;

    @PrePersist
    void prePersist() {
        this.assignedAt = LocalDateTime.now();
    }
}
