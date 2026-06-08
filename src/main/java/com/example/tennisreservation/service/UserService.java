package com.example.tennisreservation.service;

import com.example.tennisreservation.dao.UserDao;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;
import com.example.tennisreservation.exception.BadRequestException;
import com.example.tennisreservation.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

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

    public User getById(Long id) {
        return userDao.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public List<User> getAll() {
        return userDao.findAll();
    }

    public void delete(Long id) {
        if (!userDao.deleteById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
    }
}
