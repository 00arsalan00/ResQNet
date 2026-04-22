package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.RescueTeam;
import com.resqnet.resqnet_backend.entity.SkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RescueTeamRepository extends JpaRepository<RescueTeam, UUID> {
    Optional<RescueTeam> findByTeamName(String teamName);

    List<RescueTeam> findBySkillsContaining(SkillType skill);
}
