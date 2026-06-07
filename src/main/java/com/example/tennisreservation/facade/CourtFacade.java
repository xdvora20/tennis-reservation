package com.example.tennisreservation.facade;

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.dto.CourtResponse;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.mapper.CourtMapper;
import com.example.tennisreservation.service.CourtService;
import com.example.tennisreservation.service.SurfaceTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class CourtFacade {

    private final CourtService courtService;
    private final SurfaceTypeService surfaceTypeService;
    private final CourtMapper courtMapper;

    public CourtResponse create(CourtRequest request) {
        Court court = courtMapper.toEntity(request);
        court.setSurfaceType(surfaceTypeService.getById(request.surfaceTypeId()));
        return courtMapper.toResponse(courtService.create(court));
    }

    public CourtResponse update(Long id, CourtRequest request) {
        Court existing = courtService.getById(id);
        courtMapper.updateEntity(existing, request);
        existing.setSurfaceType(surfaceTypeService.getById(request.surfaceTypeId()));
        return courtMapper.toResponse(courtService.update(existing));
    }

    @Transactional(readOnly = true)
    public CourtResponse getById(Long id) {
        return courtMapper.toResponse(courtService.getById(id));
    }

    @Transactional(readOnly = true)
    public List<CourtResponse> getAll() {
        return courtMapper.toResponseList(courtService.getAll());
    }

    public void delete(Long id) {
        courtService.delete(id);
    }
}
