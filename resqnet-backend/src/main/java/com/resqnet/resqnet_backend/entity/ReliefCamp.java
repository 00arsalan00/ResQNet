package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.util.*;

@Entity
@Table(name = "relief_camps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReliefCamp {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer occupancy;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @ManyToMany(mappedBy = "reliefCamps", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Incident> incidents = new ArrayList<>();

    @ManyToMany(mappedBy = "reliefCamps", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Resource> resources = new ArrayList<>();

    @OneToMany(mappedBy = "reliefCamp", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Volunteer> volunteers = new ArrayList<>();
}