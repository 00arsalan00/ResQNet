package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType type;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point warehouseLocation;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<IncidentResource> incidentResources = new ArrayList<>();

    /**
     * One-to-many relationship to resource assignments for relief camps.
     * Use this instead of the deprecated camp_resources junction table.
     * 
     * ResourceAssignment provides rich stateful tracking including quantity,
     * status, and assignment timestamps, making it the proper entity for
     * resource allocation to camps.
     */
    @OneToMany(mappedBy = "resource")
    @Builder.Default
    private List<ResourceAssignment> resourceAssignments = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.availableQuantity == null) {
            this.availableQuantity = this.totalQuantity;
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
