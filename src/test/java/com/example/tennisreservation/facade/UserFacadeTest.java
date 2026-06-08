package com.example.tennisreservation.facade;

import static com.example.tennisreservation.utils.UserTestDataFactory.PASSWORD;
import static com.example.tennisreservation.utils.UserTestDataFactory.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dto.UserResponse;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.mapper.UserMapper;
import com.example.tennisreservation.service.UserService;
import com.example.tennisreservation.utils.UserTestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserFacade facade;

    private final UserResponse response = UserTestDataFactory.userResponse();

    @Test
    void create_createsUserAndReturnsResponse() {
        User created = UserTestDataFactory.user();
        when(userService.create(USERNAME, PASSWORD, Role.USER)).thenReturn(created);
        when(userMapper.toResponse(created)).thenReturn(response);

        assertThat(facade.create(UserTestDataFactory.createUserRequest())).isSameAs(response);
    }

    @Test
    void getById_returnsMappedResponse() {
        User user = UserTestDataFactory.user();
        when(userService.getById(1L)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        assertThat(facade.getById(1L)).isSameAs(response);
    }

    @Test
    void getAll_returnsMappedResponses() {
        List<User> users = List.of(UserTestDataFactory.user());
        List<UserResponse> responses = List.of(response);
        when(userService.getAll()).thenReturn(users);
        when(userMapper.toResponseList(users)).thenReturn(responses);

        assertThat(facade.getAll()).isEqualTo(responses);
    }

    @Test
    void delete_delegatesToService() {
        facade.delete(1L);

        verify(userService).delete(1L);
    }
}
