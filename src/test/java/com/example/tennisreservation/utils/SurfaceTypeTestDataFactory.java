package com.example.tennisreservation.utils;

import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.dto.SurfaceTypeResponse;
import com.example.tennisreservation.entity.SurfaceType;
import java.math.BigDecimal;

public final class SurfaceTypeTestDataFactory {

    public static final String NAME = "Clay";
    public static final String PRICE_PER_MINUTE = "5.00";

    private SurfaceTypeTestDataFactory() {}

    public static SurfaceType surfaceType() {
        return surfaceType(NAME, PRICE_PER_MINUTE);
    }

    public static SurfaceType surfaceType(String name, String pricePerMinute) {
        SurfaceType surface = new SurfaceType();
        surface.setName(name);
        surface.setPricePerMinute(new BigDecimal(pricePerMinute));
        return surface;
    }

    public static SurfaceTypeRequest surfaceTypeRequest() {
        return new SurfaceTypeRequest(NAME, new BigDecimal(PRICE_PER_MINUTE));
    }

    public static SurfaceTypeResponse surfaceTypeResponse() {
        return new SurfaceTypeResponse(1L, NAME, new BigDecimal(PRICE_PER_MINUTE));
    }
}
