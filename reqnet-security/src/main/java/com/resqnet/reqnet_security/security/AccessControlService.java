package com.resqnet.reqnet_security.security;

import com.resqnet.reqnet_security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("accessControl")
@RequiredArgsConstructor
public class AccessControlService {

    private final SecurityUtils securityUtils;

    public boolean isCoordinatorOf(UUID districtId) {
        return securityUtils.getCurrentUser().map(user -> {
            if (user.getRole() == Role.SUPER_ADMIN) return true;
            return user.getRole() == Role.DISTRICT_COORDINATOR &&
                   user.getDistrictId() != null &&
                   user.getDistrictId().equals(districtId);
        }).orElse(false);
    }

    public boolean isTeamInDistrict(UUID districtId) {
        return securityUtils.getCurrentUser().map(user -> {
            if (user.getRole() == Role.SUPER_ADMIN) return true;
            boolean isResponder = user.getRole() == Role.FIELD_RESCUE_TEAM ||
                                  user.getRole() == Role.VOLUNTEER;
            return isResponder &&
                   user.getDistrictId() != null &&
                   user.getDistrictId().equals(districtId);
        }).orElse(false);
    }

    public boolean isOwnerOrAdmin(UUID targetUserId) {
        return securityUtils.getCurrentUser().map(user -> {
            if (user.getRole() == Role.SUPER_ADMIN) return true;
            return user.getId().equals(targetUserId);
        }).orElse(false);
    }
}
