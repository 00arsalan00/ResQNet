package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"incident_id", "rescue_team_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class IncidentAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id",nullable=false)
    private Incident incident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rescue_team_id",nullable=false)
    private  RescueTeam rescueTeam;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    private LocalDateTime assignedAt;

    @PrePersist
    void prePersist() {
        this.assignedAt = LocalDateTime.now();
    }

}
