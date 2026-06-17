package com.resqnet.reqnet_security.security.oauth2;

import com.resqnet.reqnet_security.dto.AuthResponseDTO;
import com.resqnet.reqnet_security.entity.User;
import com.resqnet.reqnet_security.repository.UserRepository;
import com.resqnet.reqnet_security.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after OAuth2 login"));

        AuthResponseDTO tokens = authService.generateAuthResponse(user);
        
        setCookies(response, tokens);

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("accessToken", tokens.getAccessToken())
                .queryParam("refreshToken", tokens.getRefreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
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
