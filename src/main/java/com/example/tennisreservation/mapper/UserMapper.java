package com.example.tennisreservation.mapper;

import com.example.tennisreservation.dto.UserResponse;
import com.example.tennisreservation.entity.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User entity);

    List<UserResponse> toResponseList(List<User> entities);
}
