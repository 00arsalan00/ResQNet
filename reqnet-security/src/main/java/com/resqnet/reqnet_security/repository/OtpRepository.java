package com.resqnet.reqnet_security.repository;

import com.resqnet.reqnet_security.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<OtpCode, UUID> {
    Optional<OtpCode> findTopByPhoneNumberAndUsedFalseOrderByExpiryTimeDesc(String phoneNumber);
}
