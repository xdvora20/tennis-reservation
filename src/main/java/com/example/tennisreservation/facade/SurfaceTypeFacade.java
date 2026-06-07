package com.example.tennisreservation.facade;

import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.dto.SurfaceTypeResponse;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.mapper.SurfaceTypeMapper;
import com.example.tennisreservation.service.SurfaceTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class SurfaceTypeFacade {

    private final SurfaceTypeService surfaceTypeService;
    private final SurfaceTypeMapper surfaceTypeMapper;

    public SurfaceTypeResponse create(SurfaceTypeRequest request) {
        SurfaceType created = surfaceTypeService.create(surfaceTypeMapper.toEntity(request));
        return surfaceTypeMapper.toResponse(created);
    }

    public SurfaceTypeResponse update(Long id, SurfaceTypeRequest request) {
        SurfaceType existing = surfaceTypeService.getById(id);
        surfaceTypeMapper.updateEntity(existing, request);
        return surfaceTypeMapper.toResponse(surfaceTypeService.update(existing));
    }

    @Transactional(readOnly = true)
    public SurfaceTypeResponse getById(Long id) {
        return surfaceTypeMapper.toResponse(surfaceTypeService.getById(id));
    }

    @Transactional(readOnly = true)
    public List<SurfaceTypeResponse> getAll() {
        return surfaceTypeMapper.toResponseList(surfaceTypeService.getAll());
    }

    public void delete(Long id) {
        surfaceTypeService.delete(id);
    }
}
