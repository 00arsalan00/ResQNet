package com.resqnet.reqnet_security.service;

import com.resqnet.reqnet_security.dto.*;
import com.resqnet.reqnet_security.entity.*;
import com.resqnet.reqnet_security.exception.*;
import com.resqnet.reqnet_security.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OtpServiceImplementation implements OtpService {
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    private String normalizePhone(String phone) {
        return phone.replaceAll("\\s+", "");
    }

    @Override
    public void sendOtp(OtpRequestDTO request) {
        String phoneNumber = normalizePhone(request.getPhoneNumber());
        String code = String.valueOf((int)((Math.random() * 900000) + 100000));

        OtpCode otpCode = OtpCode.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();

        otpRepository.save(otpCode);

        // In production, integrate with Twilio/Msg91 here
        System.out.println("DEBUG: Sending OTP " + code + " to " + phoneNumber);
    }

    @Override
    public AuthResponseDTO verifyOtp(OtpVerificationDTO request) {
        String phoneNumber = normalizePhone(request.getPhoneNumber());
        
        OtpCode otpCode = otpRepository.findTopByPhoneNumberAndUsedFalseOrderByExpiryTimeDesc(phoneNumber)
                .orElseThrow(() -> new InvalidTokenException("No active OTP request found for " + phoneNumber));

        if (otpCode.isExpired()) {
            throw new InvalidTokenException("Access Key has expired. Please request a new one.");
        }

        if (!otpCode.getCode().equals(request.getCode().trim())) {
            throw new InvalidTokenException("The Access Key provided does not match our records.");
        }

        otpCode.setUsed(true);
        otpRepository.save(otpCode);

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("No responder account found for " + phoneNumber));

        return authService.generateAuthResponse(user);
    }
}
