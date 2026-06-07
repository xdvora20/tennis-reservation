package com.example.tennisreservation.service;

import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurfaceTypeService {

    private final SurfaceTypeDao surfaceTypeDao;

    public SurfaceType getById(Long id) {
        return surfaceTypeDao
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Surface type not found: " + id));
    }

    public List<SurfaceType> getAll() {
        return surfaceTypeDao.findAll();
    }

    public SurfaceType create(SurfaceType surfaceType) {
        return surfaceTypeDao.save(surfaceType);
    }

    public SurfaceType update(SurfaceType surfaceType) {
        return surfaceTypeDao.save(surfaceType);
    }

    public void delete(Long id) {
        if (!surfaceTypeDao.deleteById(id)) {
            throw new NotFoundException("Surface type not found: " + id);
        }
    }
}
