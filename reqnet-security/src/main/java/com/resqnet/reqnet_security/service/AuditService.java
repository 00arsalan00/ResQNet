package com.resqnet.reqnet_security.service;

import com.resqnet.reqnet_security.entity.AuditAction;
import com.resqnet.reqnet_security.entity.AuditLog;
import com.resqnet.reqnet_security.entity.User;
import com.resqnet.reqnet_security.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditRepository auditRepository;

    public void log(AuditAction action, String details) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            AuditLog log = AuditLog.builder()
                    .action(action)
                    .actorEmail(user.getEmail() != null ? user.getEmail() : user.getPhoneNumber())
                    .actorRole(user.getRole().name())
                    .districtId(user.getDistrictId())
                    .details(details)
                    .build();
            auditRepository.save(log);
        }
    }

    public void logSystemAction(AuditAction action, String details) {
        AuditLog log = AuditLog.builder()
                .action(action)
                .actorEmail("SYSTEM")
                .actorRole("SYSTEM")
                .details(details)
                .build();
        auditRepository.save(log);
    }
}
