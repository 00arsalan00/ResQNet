package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.ReliefCampRequestDTO;
import com.resqnet.resqnet_backend.dto.ReliefCampResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReliefCampService {

    ReliefCampResponseDTO createCamp(ReliefCampRequestDTO request);

    Page<ReliefCampResponseDTO> getAllCamps(Pageable pageable);

    ReliefCampResponseDTO getCampById(UUID campId);

    Integer getCapacity(UUID campId);

    Integer getOccupancy(UUID campId);

    Page<ReliefCampResponseDTO> getAvailableCamps(Pageable pageable);

    ReliefCampResponseDTO updateStatus(UUID campId, String status);

    void deleteCamp(UUID campId);
}