package com.example.tennisreservation.mapper;

import com.example.tennisreservation.dto.CourtRequest;
import com.example.tennisreservation.dto.CourtResponse;
import com.example.tennisreservation.entity.Court;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = SurfaceTypeMapper.class)
public interface CourtMapper {

    @Mapping(target = "surfaceType", ignore = true)
    Court toEntity(CourtRequest request);

    @Mapping(target = "surfaceType", ignore = true)
    void updateEntity(@MappingTarget Court entity, CourtRequest request);

    CourtResponse toResponse(Court entity);

    List<CourtResponse> toResponseList(List<Court> entities);
}
