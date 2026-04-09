package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "volunteers")
@Entity
public class Volunteer {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Column(nullable = false)
    private LocalDateTime availabilityStart;

    @Column(nullable = false)
    private LocalDateTime availabilityEnd;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "volunteer_skills", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill")
    @Builder.Default
    private List<SkillType> skills = new ArrayList<>();

    @ManyToMany(mappedBy = "volunteers", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Incident> incidents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private ReliefCamp reliefCamp;
}