package com.example.tennisreservation.service;

import com.example.tennisreservation.dao.UserDao;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String rawPassword, Role role) {
        if (userDao.findByUsername(username).isPresent()) {
            throw new BadRequestException("Username already taken: " + username);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return userDao.save(user);
    }
}
