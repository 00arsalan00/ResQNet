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

    @Override
    public void sendOtp(OtpRequestDTO request) {
        // Generate 6-digit code
        String code = String.valueOf((int)((Math.random() * 900000) + 100000));

        OtpCode otpCode = OtpCode.builder()
                .phoneNumber(request.getPhoneNumber())
                .code(code)
                .expiryTime(LocalDateTime.now().plusMinutes(5)) // Expire in the FUTURE
                .build();

        otpRepository.save(otpCode);

        // In production, this would integrate with an SMS gateway (Twilio, etc.)
        System.out.println("DEBUG: Sending OTP " + code + " to " + request.getPhoneNumber());
    }

    @Override
    public AuthResponseDTO verifyOtp(OtpVerificationDTO request) {
        OtpCode otpCode = otpRepository.findTopByPhoneNumberAndUsedFalseOrderByExpiryTimeDesc(request.getPhoneNumber())
                .orElseThrow(() -> new InvalidTokenException("No active OTP found for this number"));

        if (!otpCode.getCode().equals(request.getCode()) || otpCode.isExpired()) {
            throw new InvalidTokenException("Invalid or expired OTP code");
        }

        otpCode.setUsed(true);
        otpRepository.save(otpCode);

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new UserNotFoundException("User not registered with this phone number"));

        // Use the public shared logic for token generation
        return authService.generateAuthResponse(user);
    }
}
