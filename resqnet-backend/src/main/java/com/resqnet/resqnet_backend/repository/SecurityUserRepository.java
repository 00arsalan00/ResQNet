package com.resqnet.resqnet_backend.repository;

import com.resqnet.resqnet_backend.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, UUID> {
    Optional<SecurityUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
