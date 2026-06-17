package com.resqnet.reqnet_security.repository;

import com.resqnet.reqnet_security.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByDistrictIdOrderByTimestampDesc(UUID districtId);
    List<AuditLog> findByActorEmailOrderByTimestampDesc(String email);
}
