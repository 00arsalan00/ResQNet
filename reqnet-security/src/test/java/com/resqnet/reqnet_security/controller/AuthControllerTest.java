package com.resqnet.reqnet_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resqnet.reqnet_security.config.JwtProvider;
import com.resqnet.reqnet_security.dto.AuthResponseDTO;
import com.resqnet.reqnet_security.dto.LoginRequestDTO;
import com.resqnet.reqnet_security.dto.RegistrationRequestDTO;
import com.resqnet.reqnet_security.entity.Role;
import com.resqnet.reqnet_security.security.JwtFilter;
import com.resqnet.reqnet_security.security.oauth2.CustomOAuth2UserService;
import com.resqnet.reqnet_security.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.resqnet.reqnet_security.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_success() throws Exception {
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .role(Role.CITIZEN)
                .build();

        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .email("test@example.com")
                .role(Role.CITIZEN)
                .build();

        when(authService.registerUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("access-token"));
    }

    @Test
    void login_success() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setIdentifier("test@example.com");
        request.setPassword("password123");

        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .email("test@example.com")
                .role(Role.CITIZEN)
                .build();

        when(authService.loginUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"));
    }
}
