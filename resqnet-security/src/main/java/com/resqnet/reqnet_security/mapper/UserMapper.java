package com.resqnet.reqnet_security.mapper;

import com.resqnet.reqnet_security.dto.RegistrationRequestDTO;
import com.resqnet.reqnet_security.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegistrationRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole())
                .districtId(dto.getDistrictId())
                .build();
    }
}
