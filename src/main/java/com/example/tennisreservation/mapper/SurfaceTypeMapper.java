package com.example.tennisreservation.mapper;

import com.example.tennisreservation.dto.SurfaceTypeRequest;
import com.example.tennisreservation.dto.SurfaceTypeResponse;
import com.example.tennisreservation.entity.SurfaceType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SurfaceTypeMapper {

    SurfaceType toEntity(SurfaceTypeRequest request);

    void updateEntity(@MappingTarget SurfaceType entity, SurfaceTypeRequest request);

    SurfaceTypeResponse toResponse(SurfaceType entity);

    List<SurfaceTypeResponse> toResponseList(List<SurfaceType> entities);
}
