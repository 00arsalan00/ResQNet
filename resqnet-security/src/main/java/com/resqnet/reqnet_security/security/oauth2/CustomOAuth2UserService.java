package com.resqnet.reqnet_security.security.oauth2;

import com.resqnet.reqnet_security.entity.AuthProvider;
import com.resqnet.reqnet_security.entity.Role;
import com.resqnet.reqnet_security.entity.User;
import com.resqnet.reqnet_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getAuthProvider().equals(AuthProvider.GOOGLE)) {
                user.setAuthProvider(AuthProvider.GOOGLE);
                userRepository.save(user);
            }
        } else {
            user = User.builder()
                    .email(email)
                    .role(Role.CITIZEN)
                    .authProvider(AuthProvider.GOOGLE)
                    .enabled(true)
                    .build();
            userRepository.save(user);
        }

        return oAuth2User;
    }
}
