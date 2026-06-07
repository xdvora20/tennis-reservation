package com.example.tennisreservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourtRequest(
        @NotNull @Positive Integer courtNumber,
        @NotNull Long surfaceTypeId
) {}
