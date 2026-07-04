package com.resqnet.resqnet_backend.entity;

import org.locationtech.jts.geom.Point;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "incidents")
@Entity
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentType type;

    @Column(nullable = false)
    private Integer severity;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;
    private String street;
    private String landmark;
    private String city;
    private String district;
    private String country;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

    @Column(nullable = false)
    private String reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SecurityUser user;

    @OneToMany(mappedBy = "incident")
    private List<IncidentAssignment> assignments;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<IncidentResource> incidentResources = new ArrayList<>();
}
