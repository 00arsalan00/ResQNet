package com.resqnet.reqnet_security.service;


import com.resqnet.reqnet_security.dto.*;

public interface OtpService {

    AuthResponseDTO verifyOtp(OtpVerificationDTO request);
    void sendOtp(OtpRequestDTO request);
}
