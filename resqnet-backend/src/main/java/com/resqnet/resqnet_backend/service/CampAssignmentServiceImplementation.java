package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.entity.CampStatus;
import com.resqnet.resqnet_backend.entity.ReliefCamp;
import com.resqnet.resqnet_backend.exception.CampNotFoundException;
import com.resqnet.resqnet_backend.exception.InvalidOperationException;
import com.resqnet.resqnet_backend.repository.ReliefCampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampAssignmentServiceImplementation implements CampAssignmentService {

    private final ReliefCampRepository campRepository;

    @Override
    @Transactional
    public void assignPeople(UUID campId, int count) {

        if (count <= 0) {
            throw new InvalidOperationException("Count must be greater than 0");
        }

        ReliefCamp camp = campRepository.findById(campId)
                .orElseThrow(() -> new CampNotFoundException("Camp not found"));

        if (camp.getStatus() != CampStatus.ACTIVE) {
            throw new InvalidOperationException("Camp is not active");
        }

        int newOccupancy = camp.getOccupancy() + count;

        if (newOccupancy > camp.getCapacity()) {
            throw new InvalidOperationException("Camp capacity exceeded");
        }

        camp.setOccupancy(newOccupancy);

        if (newOccupancy == camp.getCapacity()) {
            camp.setStatus(CampStatus.FULL);
        }

        campRepository.save(camp);
    }

    @Override
    @Transactional
    public void releasePeople(UUID campId, int count) {

        if (count <= 0) {
            throw new InvalidOperationException("Count must be greater than 0");
        }

        ReliefCamp camp = campRepository.findById(campId)
                .orElseThrow(() -> new CampNotFoundException("Camp not found"));

        int newOccupancy = camp.getOccupancy() - count;

        if (newOccupancy < 0) {
            throw new InvalidOperationException("Cannot release more people than present");
        }

        camp.setOccupancy(newOccupancy);

        if (camp.getStatus() == CampStatus.FULL && newOccupancy < camp.getCapacity()) {
            camp.setStatus(CampStatus.ACTIVE);
        }

        campRepository.save(camp);
    }
}