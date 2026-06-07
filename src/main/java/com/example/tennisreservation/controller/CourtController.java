package com.example.tennisreservation.controller;

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.dto.CourtResponse;
import com.example.tennisreservation.facade.CourtFacade;
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
@RequestMapping("/api/courts")
@RequiredArgsConstructor
@Tag(name = "Courts", description = "Manage tennis courts")
public class CourtController {

    private final CourtFacade courtFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourtResponse create(@Valid @RequestBody CourtRequest request) {
        return courtFacade.create(request);
    }

    @GetMapping
    public List<CourtResponse> getAll() {
        return courtFacade.getAll();
    }

    @GetMapping("/{id}")
    public CourtResponse getById(@PathVariable Long id) {
        return courtFacade.getById(id);
    }

    @PutMapping("/{id}")
    public CourtResponse update(@PathVariable Long id, @Valid @RequestBody CourtRequest request) {
        return courtFacade.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courtFacade.delete(id);
    }
}
