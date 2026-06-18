package com.resqnet.reqnet_security.security;

import com.resqnet.reqnet_security.entity.Role;
import com.resqnet.reqnet_security.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityUtils {

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return Optional.of((User) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().map(User::getId).orElse(null);
    }

    public UUID getCurrentUserDistrictId() {
        return getCurrentUser().map(User::getDistrictId).orElse(null);
    }

    public boolean hasRole(Role role) {
        return getCurrentUser()
                .map(user -> user.getRole() == role)
                .orElse(false);
    }
}
