package com.resqnet.reqnet_security.service;

import com.resqnet.reqnet_security.config.JwtProvider;
import com.resqnet.reqnet_security.dto.*;
import com.resqnet.reqnet_security.entity.*;
import com.resqnet.reqnet_security.exception.*;
import com.resqnet.reqnet_security.mapper.UserMapper;
import com.resqnet.reqnet_security.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    @Override
    public AuthResponseDTO registerUser(RegistrationRequestDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        if (user.getRole() == null) {
            user.setRole(Role.CITIZEN);
        }
        if (user.getAuthProvider() == null) {
            user.setAuthProvider(AuthProvider.LOCAL);
        }
        
        userRepository.save(user);
        return generateAuthResponse(user);
    }

    @Override
    public AuthResponseDTO loginUser(LoginRequestDTO request) {
        String identifier = request.getIdentifier();
        
        User user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhoneNumber(identifier))
                .orElseThrow(() -> new UserNotFoundException("User not found with identifier: " + identifier));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return generateAuthResponse(user);
    }

    @Override
    public AuthResponseDTO refreshToken(TokenRefreshRequestDTO request) {
        RefreshToken tokenEntity = tokenRepository.findByToken(request.getRefreshToken())
                .filter(t -> !t.isRevoked() && !t.isExpired())
                .orElseThrow(() -> new InvalidTokenException("Token invalid or expired"));

        tokenEntity.setRevoked(true);
        tokenRepository.save(tokenEntity);

        return generateAuthResponse(tokenEntity.getUser());
    }

    private AuthResponseDTO generateAuthResponse(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        
        tokenRepository.save(refreshTokenEntity);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .districtId(user.getDistrictId())
                .email(user.getEmail())
                .build();
    }
}
