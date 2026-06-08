package com.example.tennisreservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tennisreservation.dao.UserDao;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.exception.BadRequestException;
import com.example.tennisreservation.utils.UserTestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void create_newUsername_encodesPasswordAndSaves() {
        when(userDao.findByUsername("alice")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("s3cret")).thenReturn("hashed");
        when(userDao.save(any(User.class))).thenAnswer(call -> call.getArgument(0));

        User created = userService.create("alice", "s3cret", Role.USER);

        assertThat(created.getUsername()).isEqualTo("alice");
        assertThat(created.getPassword()).isEqualTo("hashed");
        assertThat(created.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void create_duplicateUsername_throwsBadRequest() {
        when(userDao.findByUsername("alice"))
                .thenReturn(Optional.of(UserTestDataFactory.user()));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.create("alice", "s3cret", Role.USER));
        verify(userDao, never()).save(any());
    }
}
