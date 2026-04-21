package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.repository.ReliefCampRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampAssignmentServiceTest {

    @Mock
    private ReliefCampRepository repository;

    @InjectMocks
    private CampAssignmentServiceImplementation service;

    @Test
    void assignPeople_success() {
        UUID campId = UUID.randomUUID();

        ReliefCamp camp = new ReliefCamp();
        camp.setCapacity(100);
        camp.setOccupancy(50);
        camp.setStatus(CampStatus.ACTIVE);

        when(repository.findById(campId)).thenReturn(Optional.of(camp));

        service.assignPeople(campId, 20);

        assertEquals(70, camp.getOccupancy());
        verify(repository).save(camp);
    }

    @Test
    void assignPeople_exceedsCapacity() {
        UUID campId = UUID.randomUUID();

        ReliefCamp camp = new ReliefCamp();
        camp.setCapacity(100);
        camp.setOccupancy(90);
        camp.setStatus(CampStatus.ACTIVE);

        when(repository.findById(campId)).thenReturn(Optional.of(camp));

        assertThrows(InvalidOperationException.class,
                () -> service.assignPeople(campId, 20));
    }

    @Test
    void assignPeople_inactiveCamp() {
        UUID campId = UUID.randomUUID();

        ReliefCamp camp = new ReliefCamp();
        camp.setCapacity(100);
        camp.setOccupancy(50);
        camp.setStatus(CampStatus.INACTIVE);

        when(repository.findById(campId)).thenReturn(Optional.of(camp));

        assertThrows(InvalidOperationException.class,
                () -> service.assignPeople(campId, 10));
    }

    @Test
    void releasePeople_success() {
        UUID campId = UUID.randomUUID();

        ReliefCamp camp = new ReliefCamp();
        camp.setCapacity(100);
        camp.setOccupancy(50);
        camp.setStatus(CampStatus.FULL);

        when(repository.findById(campId)).thenReturn(Optional.of(camp));

        service.releasePeople(campId, 20);

        assertEquals(30, camp.getOccupancy());
        verify(repository).save(camp);
    }

    @Test
    void releasePeople_belowZero() {
        UUID campId = UUID.randomUUID();

        ReliefCamp camp = new ReliefCamp();
        camp.setCapacity(100);
        camp.setOccupancy(10);
        camp.setStatus(CampStatus.ACTIVE);

        when(repository.findById(campId)).thenReturn(Optional.of(camp));

        assertThrows(InvalidOperationException.class,
                () -> service.releasePeople(campId, 20));
    }
}