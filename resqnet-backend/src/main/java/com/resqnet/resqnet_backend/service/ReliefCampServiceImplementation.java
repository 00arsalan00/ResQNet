package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.ReliefCampRequestDTO;
import com.resqnet.resqnet_backend.dto.ReliefCampResponseDTO;
import com.resqnet.resqnet_backend.entity.*;
import com.resqnet.resqnet_backend.exception.*;
import com.resqnet.resqnet_backend.mapper.ReliefCampMapper;
import com.resqnet.resqnet_backend.repository.ReliefCampRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReliefCampServiceImplementation implements ReliefCampService {

    private final ReliefCampRepository campRepository;
    private final ReliefCampMapper campMapper;
    private final GeometryFactory geometryFactory;

    @Override
    public ReliefCampResponseDTO createCamp(ReliefCampRequestDTO request) {

        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );

        ReliefCamp camp = ReliefCamp.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .occupancy(0)
                .location(point)
                .status(CampStatus.ACTIVE)
                .build();

        return campMapper.toResponse(campRepository.save(camp));
    }

    @Override
    public Page<ReliefCampResponseDTO> getAllCamps(Pageable pageable) {
        return campRepository.findAll(pageable)
                .map(campMapper::toResponse);
    }

    @Override
    public ReliefCampResponseDTO getCampById(UUID campId) {
        return campMapper.toResponse(getEntity(campId));
    }

    @Override
    public Integer getCapacity(UUID campId) {
        return getEntity(campId).getCapacity();
    }

    @Override
    public Integer getOccupancy(UUID campId) {
        return getEntity(campId).getOccupancy();
    }

    @Override
    public Page<ReliefCampResponseDTO> getAvailableCamps(Pageable pageable) {
        return campRepository.findAvailableCamps(pageable)
                .map(campMapper::toResponse);
    }
    @Override
    public ReliefCampResponseDTO updateStatus(UUID campId, String status) {

        ReliefCamp camp = getEntity(campId);

        try {
            CampStatus newStatus = CampStatus.valueOf(status.toUpperCase());
            camp.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusTransitionException("Invalid camp status");
        }

        return campMapper.toResponse(campRepository.save(camp));
    }

    @Override
    public void deleteCamp(UUID campId) {
        campRepository.deleteById(campId);
    }

    private ReliefCamp getEntity(UUID id) {
        return campRepository.findById(id)
                .orElseThrow(() -> new CampNotFoundException("Camp not found"));
    }
}