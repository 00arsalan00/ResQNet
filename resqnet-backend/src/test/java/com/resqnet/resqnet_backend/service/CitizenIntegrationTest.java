package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.IncidentRequestDTO;
import com.resqnet.resqnet_backend.entity.IncidentType;
import com.resqnet.resqnet_backend.entity.UserRole;
import com.resqnet.resqnet_backend.repository.SecurityUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class CitizenIntegrationTest {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private SecurityUserRepository userRepo;

    @Test
    void testSilentRegistration() {
        String testEmail = "victim@example.com";

        IncidentRequestDTO request = new IncidentRequestDTO();
        request.setEmail(testEmail);
        request.setReporter("Citizen Test");
        request.setType(IncidentType.FIRE);      // or any valid IncidentType
        request.setDescription("Help needed immediately");
        request.setCity("Delhi");
        request.setDistrict("New Delhi");
        request.setCountry("India");

        incidentService.register(request);

        assertTrue(userRepo.existsByEmail(testEmail));
        assertEquals(
                UserRole.CITIZEN,
                userRepo.findByEmail(testEmail).get().getRole()
        );
    }
}