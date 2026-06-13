package com.resqnet.reqnet_security.dto;

import com.resqnet.reqnet_security.entity.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
public class RegistrationRequestDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    private String phoneNumber;
    private Role role;
    private UUID districtId;
}
