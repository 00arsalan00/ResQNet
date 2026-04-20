package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.CampStatus;
import com.resqnet.resqnet_backend.entity.ReliefCamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface ReliefCampRepository extends JpaRepository<ReliefCamp, UUID> {

    Page<ReliefCamp> findByStatus(CampStatus status, Pageable pageable);

    @Query("""
       SELECT c FROM ReliefCamp c
       WHERE c.status = 'ACTIVE'
       AND c.occupancy < c.capacity
       """)
    Page<ReliefCamp> findAvailableCamps(Pageable pageable);

    long countByStatus(CampStatus status);

    boolean existsById(UUID id);
}