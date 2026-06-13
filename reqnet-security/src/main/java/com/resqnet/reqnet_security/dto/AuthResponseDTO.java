package com.resqnet.reqnet_security.dto;

import com.resqnet.reqnet_security.entity.Role;
import lombok.*;
import java.util.UUID;

@Data
@Builder
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Role role;
    private UUID districtId;
    private String email;
}