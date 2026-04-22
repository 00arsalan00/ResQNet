package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerAssignmentServiceTest {

    @Mock
    private VolunteerAssignmentRepository assignmentRepository;

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerAssignmentServiceImplementation service;

    @Test
    void testAssignVolunteer_success() {
        UUID incidentId = UUID.randomUUID();
        UUID volunteerId = UUID.randomUUID();

        Volunteer volunteer = new Volunteer();
        volunteer.setStatus(VolunteerStatus.AVAILABLE);

        Incident incident = new Incident();

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(assignmentRepository.existsByIncident_IdAndVolunteer_Id(incidentId, volunteerId))
                .thenReturn(false);

        VolunteerAssignment saved = new VolunteerAssignment();
        when(assignmentRepository.save(any())).thenReturn(saved);

        VolunteerAssignment result = service.assignVolunteer(incidentId, volunteerId);

        assertNotNull(result);
        verify(assignmentRepository).save(any());
    }

    @Test
    void testAssignVolunteer_alreadyAssigned() {
        UUID incidentId = UUID.randomUUID();
        UUID volunteerId = UUID.randomUUID();

        Volunteer volunteer = new Volunteer();
        volunteer.setStatus(VolunteerStatus.AVAILABLE);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(new Incident()));
        when(assignmentRepository.existsByIncident_IdAndVolunteer_Id(incidentId, volunteerId))
                .thenReturn(true);

        assertThrows(VolunteerAlreadyAssignedToIncidentException.class,
                () -> service.assignVolunteer(incidentId, volunteerId));
    }
}