package com.resqnet.reqnet_security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpRequestDTO {

    @NotBlank(message = "Phone Number is Required")
    private String phoneNumber;

}
