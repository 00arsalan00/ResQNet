package com.resqnet.resqnet_backend.entity;

import lombok.*;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resources")
@Entity
public class Resource {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point warehouseLocation;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<IncidentResource> incidentResources = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "resource_relief_camp",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "camp_id")
    )
    @Builder.Default
    private List<ReliefCamp> reliefCamps = new ArrayList<>();
}
