package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.Volunteer;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VolunteerRepository extends JpaRepository<Volunteer, UUID> {

    Page<Volunteer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Volunteer> findBySkillsContaining(SkillType skill, Pageable pageable);

    boolean existsByIncidentIdAndVolunteerId(UUID incidentId, UUID volunteerId);
}