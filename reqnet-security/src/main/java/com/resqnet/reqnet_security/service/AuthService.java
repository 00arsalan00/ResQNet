package com.resqnet.reqnet_security.service;

import com.resqnet.reqnet_security.dto.*;
import com.resqnet.reqnet_security.entity.User;

public interface AuthService {
    AuthResponseDTO registerUser(RegistrationRequestDTO request);
    AuthResponseDTO loginUser(LoginRequestDTO request);
    AuthResponseDTO refreshToken(TokenRefreshRequestDTO request);
    AuthResponseDTO generateAuthResponse(User user);
}
