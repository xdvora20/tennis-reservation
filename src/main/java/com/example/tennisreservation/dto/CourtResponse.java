package com.example.tennisreservation.dto;

public record CourtResponse(
        Long id,
        Integer courtNumber,
        SurfaceTypeResponse surfaceType
) {}
