package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    List<Incident> findByUserIdOrderByStatusDesc(UUID userId);

    @Query(value = "SELECT * FROM incidents i WHERE i.status != 'RESOLVED' ORDER BY i.location <-> :userLocation", nativeQuery = true)
    List<Incident> findNearestIncidents(@Param("userLocation") org.locationtech.jts.geom.Point userLocation);
}
