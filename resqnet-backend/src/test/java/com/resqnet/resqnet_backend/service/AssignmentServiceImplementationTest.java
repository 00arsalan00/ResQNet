package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.AssignmentRequestDTO;
import com.resqnet.resqnet_backend.dto.AssignmentResponseDTO;
import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.mapper.AssignmentMapper;
import com.resqnet.resqnet_backend.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Assignment Test")
public class AssignmentServiceImplementationTest {
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private AssignmentMapper assignmentMapper;
    @InjectMocks
    private AssignmentServiceImplementation assignmentService;
    @Mock
    private IncidentRepository incidentRepository;
    @Mock
    private RescueTeamRepository rescueTeamRepository;


    private final UUID incidentId = UUID.randomUUID();
    private final UUID teamId = UUID.randomUUID();

    @Test
    void assign() {

        Incident incident = new Incident();
        RescueTeam rescueTeam = new RescueTeam();
        rescueTeam.setCapacity(5);

        when(incidentRepository.findById(incidentId))
                .thenReturn(Optional.of(incident));

        when(rescueTeamRepository.findById(teamId))
                .thenReturn(Optional.of(rescueTeam));

        when(assignmentRepository.existsByIncidentIdAndRescueTeamId(incidentId, teamId))
                .thenReturn(false);

        when(assignmentRepository.countByRescueTeamIdAndStatusIn(any(), any()))
                .thenReturn(1L);

        when(assignmentRepository.save(any(IncidentAssignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        IncidentAssignment result = assignmentService.assignTeam(incidentId, teamId);

        assertNotNull(result);
        assertEquals(AssignmentStatus.ASSIGNED, result.getStatus());
        assertEquals(incident, result.getIncident());
        assertEquals(rescueTeam, result.getRescueTeam());

        verify(assignmentRepository, times(1)).save(any());
    }

    @Test
    void assignTeamIncidentNotFound() {
        when(incidentRepository.findById(incidentId)).thenReturn(Optional.empty());
        assertThrows(IncidentNotFoundException.class, () -> assignmentService.assignTeam(incidentId, teamId));
    }

    @Test
    void assignTeamRescueTeamNotFound() {
        when(incidentRepository.findById(incidentId)).thenReturn(Optional.empty());
        assertThrows(IncidentNotFoundException.class, () -> assignmentService.assignTeam(incidentId, teamId));
    }
    @Test
    void capacityExceeded() {
        Incident incident = new Incident();
        RescueTeam rescueTeam = new RescueTeam();
        rescueTeam.setCapacity(2);

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(rescueTeamRepository.findById(teamId)).thenReturn(Optional.of(rescueTeam));
        when(assignmentRepository.existsByIncidentIdAndRescueTeamId(incidentId, teamId)).thenReturn(false);
        when(assignmentRepository.countByRescueTeamIdAndStatusIn(any(), any())).thenReturn(2L);

        assertThrows(CapacityExceededException.class,() -> assignmentService.assignTeam(incidentId, teamId));
    }

    @Test
    void alreadyAssigned() {
        Incident incident = new Incident();
        RescueTeam rescueTeam = new RescueTeam();
        rescueTeam.setCapacity(2);
        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(rescueTeamRepository.findById(teamId)).thenReturn(Optional.of(rescueTeam));
        when(assignmentRepository.existsByIncidentIdAndRescueTeamId(incidentId, teamId)).thenReturn(true);

        assertThrows(TeamAlreadyAssignedToIncidentException.class,() -> assignmentService.assignTeam(incidentId, teamId));
    }

    @Test
    void getAllAssignments() {
        Pageable pageable = PageRequest.of(0, 2);
        IncidentAssignment assignment1 = new IncidentAssignment();
        IncidentAssignment assignment2 = new IncidentAssignment();
        Page<IncidentAssignment> page = new PageImpl<>(List.of(assignment1, assignment2));

        AssignmentResponseDTO dto1 = new AssignmentResponseDTO();
        AssignmentResponseDTO dto2 = new AssignmentResponseDTO();

        when(assignmentRepository.findAll(pageable)).thenReturn(page);
        when(assignmentMapper.toResponse(assignment1)).thenReturn(dto1);
        when(assignmentMapper.toResponse(assignment2)).thenReturn(dto2);

        Page<AssignmentResponseDTO> result = assignmentService.getAllAssignments(pageable);
        assertNotNull(result);
        assertEquals(2,result.getContent().size());

        verify(assignmentRepository).findAll(pageable);
        verify(assignmentMapper, times(2)).toResponse(any());


    }

    @Test
    void getAllAssignments_empty_shouldThrow() {

        Pageable pageable = PageRequest.of(0, 2);

        Page<IncidentAssignment> emptyPage =
                new PageImpl<>(List.of());

        when(assignmentRepository.findAll(pageable))
                .thenReturn(emptyPage);

        assertThrows(AssignmentNotFound.class,
                () -> assignmentService.getAllAssignments(pageable));
    }

    @Test
    void updateAssignment_success() {

        UUID assignmentId = UUID.randomUUID();

        IncidentAssignment assignment = new IncidentAssignment();
        assignment.setStatus(AssignmentStatus.ASSIGNED);

        Incident incident = new Incident();
        RescueTeam team = new RescueTeam();

        AssignmentRequestDTO request = new AssignmentRequestDTO();
        request.setIncidentId(UUID.randomUUID());
        request.setTeamId(UUID.randomUUID());
        request.setStatus(AssignmentStatus.IN_PROGRESS);

        AssignmentResponseDTO responseDTO =
                AssignmentResponseDTO.builder().build();


        when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.of(assignment));

        when(incidentRepository.findById(request.getIncidentId()))
                .thenReturn(Optional.of(incident));

        when(rescueTeamRepository.findById(request.getTeamId()))
                .thenReturn(Optional.of(team));

        when(assignmentRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(assignmentMapper.toResponse(any()))
                .thenReturn(responseDTO);


        AssignmentResponseDTO result =
                assignmentService.updateAssignment(assignmentId, request);

        assertNotNull(result);
        assertEquals(AssignmentStatus.IN_PROGRESS, assignment.getStatus());

        verify(assignmentRepository).save(any());
        verify(assignmentMapper).toResponse(any());
    }

    @Test
    void updateAssignment_invalidStatusTransition() {

        UUID assignmentId = UUID.randomUUID();

        IncidentAssignment assignment = new IncidentAssignment();
        assignment.setStatus(AssignmentStatus.COMPLETED);

        AssignmentRequestDTO request = new AssignmentRequestDTO();
        request.setIncidentId(UUID.randomUUID());
        request.setTeamId(UUID.randomUUID());
        request.setStatus(AssignmentStatus.ASSIGNED);

        when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.of(assignment));

        when(incidentRepository.findById(request.getIncidentId()))
                .thenReturn(Optional.of(new Incident()));

        when(rescueTeamRepository.findById(request.getTeamId()))
                .thenReturn(Optional.of(new RescueTeam()));

        assertThrows(InvalidStatusTransitionException.class,
                () -> assignmentService.updateAssignment(assignmentId, request));
    }

    @Test
    void updateAssignment_assignmentNotFound() {

        UUID assignmentId = UUID.randomUUID();

        when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.empty());

        AssignmentRequestDTO request = new AssignmentRequestDTO();

        assertThrows(AssignmentNotFound.class,
                () -> assignmentService.updateAssignment(assignmentId, request));
    }

    @Test
    void deleteAssignment_success() {

        UUID assignmentId = UUID.randomUUID();

        IncidentAssignment assignment = new IncidentAssignment();

        when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.of(assignment));

        doNothing().when(assignmentRepository).delete(assignment);

        assignmentService.deleteAssignment(assignmentId);

        verify(assignmentRepository, times(1)).findById(assignmentId);
        verify(assignmentRepository, times(1)).delete(assignment);
    }

    @Test
    void deleteAssignment_notFound() {

        UUID assignmentId = UUID.randomUUID();


        when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.empty());


        assertThrows(AssignmentNotFound.class,
                () -> assignmentService.deleteAssignment(assignmentId));

        verify(assignmentRepository, never()).delete(any());
    }




}
