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
import com.example.tennisreservation.exception.NotFoundException;
import com.example.tennisreservation.utils.UserTestDataFactory;
import java.util.List;
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

    @Test
    void getById_existing_returnsUser() {
        User user = UserTestDataFactory.user();
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        assertThat(userService.getById(1L)).isSameAs(user);
    }

    @Test
    void getById_missing_throwsNotFound() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> userService.getById(99L));
    }

    @Test
    void getAll_delegatesToDao() {
        List<User> users = List.of(UserTestDataFactory.user());
        when(userDao.findAll()).thenReturn(users);

        assertThat(userService.getAll()).isEqualTo(users);
    }

    @Test
    void delete_existing_succeeds() {
        when(userDao.deleteById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userDao).deleteById(1L);
    }

    @Test
    void delete_missing_throwsNotFound() {
        when(userDao.deleteById(99L)).thenReturn(false);

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> userService.delete(99L));
    }
}
