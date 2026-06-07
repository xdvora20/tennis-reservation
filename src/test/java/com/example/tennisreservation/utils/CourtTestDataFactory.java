package com.example.tennisreservation.utils;

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.dto.CourtResponse;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.SurfaceType;

public final class CourtTestDataFactory {

    public static final int COURT_NUMBER = 1;
    public static final long SURFACE_TYPE_ID = 1L;

    private CourtTestDataFactory() {}

    public static Court court() {
        return court(COURT_NUMBER, SurfaceTypeTestDataFactory.surfaceType());
    }

    public static Court court(int courtNumber, SurfaceType surfaceType) {
        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);
        return court;
    }

    public static CourtRequest courtRequest() {
        return new CourtRequest(COURT_NUMBER, SURFACE_TYPE_ID);
    }

    public static CourtResponse courtResponse() {
        return new CourtResponse(1L, COURT_NUMBER, SurfaceTypeTestDataFactory.surfaceTypeResponse());
    }
}
