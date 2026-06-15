package com.resqnet.reqnet_security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpVerificationDTO {
    @NotBlank(message = "Phone Number is Required")
    private String phoneNumber;
    @NotBlank(message = "OTP code is Required")
    private String code;
}
