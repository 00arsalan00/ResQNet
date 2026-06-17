package com.resqnet.reqnet_security.controller;

import com.resqnet.reqnet_security.dto.*;
import com.resqnet.reqnet_security.service.OtpService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<AuthResponseDTO> verifyOtp(@Valid @RequestBody OtpVerificationDTO request, HttpServletResponse response) {
        AuthResponseDTO authResponse = otpService.verifyOtp(request);
        setCookies(response, authResponse);
        return ResponseEntity.ok(authResponse);
    }

    private void setCookies(HttpServletResponse response, AuthResponseDTO authResponse) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", authResponse.getAccessToken())
                .httpOnly(true)
                .secure(false) // Set to true in production
                .path("/")
                .maxAge(1500) // 25 minutes
                .sameSite("Strict")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // Set to true in production
                .path("/")
                .maxAge(604800) // 7 days
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
