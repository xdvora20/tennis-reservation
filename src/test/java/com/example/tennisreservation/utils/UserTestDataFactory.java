package com.example.tennisreservation.utils;

import com.example.tennisreservation.dto.CreateUserRequest;
import com.example.tennisreservation.dto.UserResponse;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.User;

public final class UserTestDataFactory {

    public static final long ID = 1L;
    public static final String USERNAME = "alice";
    public static final String PASSWORD = "s3cret";
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

    public static CreateUserRequest createUserRequest() {
        return new CreateUserRequest(USERNAME, PASSWORD, Role.USER);
    }

    public static UserResponse userResponse() {
        return new UserResponse(ID, USERNAME, Role.USER);
    }
}
