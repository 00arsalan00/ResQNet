package com.resqnet.resqnet_backend.service;

import com.resqnet.resqnet_backend.dto.VolunteerRequestDTO;
import com.resqnet.resqnet_backend.dto.VolunteerResponseDTO;
import com.resqnet.resqnet_backend.entity.SkillType;
import com.resqnet.resqnet_backend.entity.Volunteer;
import com.resqnet.resqnet_backend.exception.VolunteerNotFoundException;
import com.resqnet.resqnet_backend.mapper.VolunteerMapper;
import com.resqnet.resqnet_backend.repository.VolunteerRepository;
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
public class VolunteerServiceImplementation implements VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerMapper volunteerMapper;
    private final GeometryFactory geometryFactory;

    @Override
    public VolunteerResponseDTO getById(UUID id) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found"));

        return volunteerMapper.toResponse(volunteer);
    }

    @Override
    public Page<VolunteerResponseDTO> getAllVolunteers(Pageable pageable) {
        return volunteerRepository.findAll(pageable)
                .map(volunteerMapper::toResponse);
    }

    @Override
    public Page<VolunteerResponseDTO> getByName(String name, Pageable pageable) {
        return volunteerRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(volunteerMapper::toResponse);
    }

    @Override
    public Page<VolunteerResponseDTO> getBySkill(SkillType skill, Pageable pageable) {
        return volunteerRepository.findBySkillsContaining(skill, pageable)
                .map(volunteerMapper::toResponse);
    }

    @Override
    public VolunteerResponseDTO registerVolunteer(VolunteerRequestDTO request) {
        Volunteer volunteer = volunteerMapper.toEntity(request);
        Volunteer saved = volunteerRepository.save(volunteer);
        return volunteerMapper.toResponse(saved);
    }

    @Override
    public VolunteerResponseDTO updateVolunteer(UUID id, VolunteerRequestDTO request) {

        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found"));

        volunteer.setName(request.getName());
        volunteer.setContactInfo(request.getContactInfo());
        volunteer.setSkills(request.getSkills());
        volunteer.setStatus(request.getStatus());
        volunteer.setAvailabilityStart(request.getAvailabilityStart());
        volunteer.setAvailabilityEnd(request.getAvailabilityEnd());


        Point point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );
        volunteer.setLocation(point);

        Volunteer updated = volunteerRepository.save(volunteer);

        return volunteerMapper.toResponse(updated);
    }

    @Override
    public void deleteVolunteer(UUID id) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found"));

        volunteerRepository.delete(volunteer);
    }

}