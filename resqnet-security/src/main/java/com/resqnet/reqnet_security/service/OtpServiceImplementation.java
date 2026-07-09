package com.resqnet.reqnet_security.service;

import com.resqnet.reqnet_security.dto.*;
import com.resqnet.reqnet_security.entity.*;
import com.resqnet.reqnet_security.exception.*;
import com.resqnet.reqnet_security.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OtpServiceImplementation implements OtpService {
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final JavaMailSender mailSender;

    private String normalizeIdentifier(String input) {
        return input.replaceAll("\\s+", "");
    }

    @Override
    public void sendOtp(OtpRequestDTO request) {
        String identifier = normalizeIdentifier(request.getPhoneNumber());
        String code = String.valueOf((int)((Math.random() * 900000) + 100000));

        OtpCode otpCode = OtpCode.builder()
                .phoneNumber(identifier)
                .code(code)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();

        otpRepository.save(otpCode);

        if (identifier.contains("@")) {
            sendEmail(identifier, code);
        } else {
            System.out.println("DEBUG: Sending SMS OTP " + code + " to " + identifier);
        }
    }

    @Async
    protected void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("ResQNet - Security Access Key");
        message.setText("Your operational access key is: " + code + "\nValid for 5 minutes.");
        mailSender.send(message);
    }

    @Override
    public AuthResponseDTO verifyOtp(OtpVerificationDTO request) {
        String identifier = normalizeIdentifier(request.getPhoneNumber());
        
        OtpCode otpCode = otpRepository.findTopByPhoneNumberAndUsedFalseOrderByExpiryTimeDesc(identifier)
                .orElseThrow(() -> new InvalidTokenException("No active OTP request found."));

        if (otpCode.isExpired()) {
            throw new InvalidTokenException("Access Key has expired.");
        }

        if (!otpCode.getCode().equals(request.getCode().trim())) {
            throw new InvalidTokenException("Invalid Access Key.");
        }

        otpCode.setUsed(true);
        otpRepository.save(otpCode);

        User user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhoneNumber(identifier))
                .orElseThrow(() -> new UserNotFoundException("No responder account found."));

        return authService.generateAuthResponse(user);
    }
}
