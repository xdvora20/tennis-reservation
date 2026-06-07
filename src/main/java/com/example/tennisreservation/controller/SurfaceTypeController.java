package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.dto.SurfaceTypeResponse;
import com.example.tennisreservation.facade.SurfaceTypeFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/surface-types")
@RequiredArgsConstructor
@Tag(name = "Surface types", description = "Manage court surface types and their per-minute prices")
public class SurfaceTypeController {

    private final SurfaceTypeFacade surfaceTypeFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SurfaceTypeResponse create(@Valid @RequestBody SurfaceTypeRequest request) {
        return surfaceTypeFacade.create(request);
    }

    @GetMapping
    public List<SurfaceTypeResponse> getAll() {
        return surfaceTypeFacade.getAll();
    }

    @GetMapping("/{id}")
    public SurfaceTypeResponse getById(@PathVariable Long id) {
        return surfaceTypeFacade.getById(id);
    }

    @PutMapping("/{id}")
    public SurfaceTypeResponse update(
            @PathVariable Long id, @Valid @RequestBody SurfaceTypeRequest request) {
        return surfaceTypeFacade.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        surfaceTypeFacade.delete(id);
    }
}
