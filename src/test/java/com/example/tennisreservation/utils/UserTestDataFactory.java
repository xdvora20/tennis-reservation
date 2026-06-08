package com.example.tennisreservation.utils;

import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;

public final class UserTestDataFactory {

    public static final String USERNAME = "alice";
    public static final String PASSWORD_HASH = "hashed-password";

    private UserTestDataFactory() {}

    public static User user() {
        return user(USERNAME, PASSWORD_HASH, Role.USER);
    }

    public static User user(String username, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
