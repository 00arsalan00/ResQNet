package com.resqnet.reqnet_security.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class LoginRequestDTO {

    @NotNull(message = "Provide either of field")
    private String username;
    private String email;
    private String phoneNumber;

    @NotNull(message = "Provide Password")
    private String password;

}



