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
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentType type;

    @Column(nullable = false)
    private Integer severity;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

    @Column(nullable = false)
    private String reporter;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "incident_rescue_team",
            joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @Builder.Default
    private List<RescueTeam> rescueTeams = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "incident_volunteer",
            joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "volunteer_id")
    )
    @Builder.Default
    private List<Volunteer> volunteers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "incident_relief_camp",
            joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "camp_id")
    )
    @Builder.Default
    private List<ReliefCamp> reliefCamps = new ArrayList<>();

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<IncidentResource> incidentResources = new ArrayList<>();
}