package com.resqnet.reqnet_security.controller;

import com.resqnet.reqnet_security.dto.*;
import com.resqnet.reqnet_security.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegistrationRequestDTO request, HttpServletResponse response) {
        AuthResponseDTO authResponse = authService.registerUser(request);
        setCookies(response, authResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse response) {
        AuthResponseDTO authResponse = authService.loginUser(request);
        setCookies(response, authResponse);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody TokenRefreshRequestDTO request, HttpServletResponse response) {
        AuthResponseDTO authResponse = authService.refreshToken(request);
        setCookies(response, authResponse);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(HttpServletResponse response) {
        clearCookies(response);
        return ResponseEntity.ok(new MessageResponseDTO("Logged out successfully"));
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

    private void clearCookies(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
