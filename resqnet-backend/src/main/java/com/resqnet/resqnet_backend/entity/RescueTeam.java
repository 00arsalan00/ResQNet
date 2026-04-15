package com.resqnet.resqnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.util.*;

@Entity
@Table(name = "rescue_teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String captainName;

    @Column(nullable = false, unique = true)
    private String contactInfo;

    @Column(nullable = false)
    private Integer capacity;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamStatus status;

    @OneToMany(mappedBy = "rescueTeam")
    private List<IncidentAssignment> assignments;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "rescue_team_skills", joinColumns = @JoinColumn(name = "team_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill")
    @Builder.Default
    private List<SkillType> skills = new ArrayList<>();

}