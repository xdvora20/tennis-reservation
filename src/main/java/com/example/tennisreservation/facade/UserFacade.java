package com.example.tennisreservation.facade;

import com.example.tennisreservation.dto.CreateUserRequest;
import com.example.tennisreservation.dto.UserResponse;
import com.example.tennisreservation.mapper.UserMapper;
import com.example.tennisreservation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserResponse create(CreateUserRequest request) {
        return userMapper.toResponse(
                userService.create(request.username(), request.password(), request.role()));
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userMapper.toResponse(userService.getById(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userMapper.toResponseList(userService.getAll());
    }

    public void delete(Long id) {
        userService.delete(id);
    }
}
