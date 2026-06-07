package com.example.tennisreservation.mapper;

import com.example.tennisreservation.dto.ReservationResponse;
import com.example.tennisreservation.entity.Reservation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "courtNumber", source = "court.courtNumber")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "phoneNumber", source = "customer.phoneNumber")
    ReservationResponse toResponse(Reservation entity);

    List<ReservationResponse> toResponseList(List<Reservation> entities);
}
