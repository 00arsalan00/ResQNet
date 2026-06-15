package com.resqnet.reqnet_security.controller;

import com.resqnet.reqnet_security.dto.*;
import com.resqnet.reqnet_security.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponseDTO> sendOtp(@Valid @RequestBody OtpRequestDTO request) {
        otpService.sendOtp(request);
        return ResponseEntity.ok(new MessageResponseDTO("OTP sent successfully to: " + request.getPhoneNumber()));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponseDTO> verifyOtp(@Valid @RequestBody OtpVerificationDTO request) {
        return ResponseEntity.ok(otpService.verifyOtp(request));
    }
}
