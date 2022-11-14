package com.luis.diaz.authentication.services;

import com.luis.diaz.authentication.dto.requests.UserRequest;
import com.luis.diaz.authentication.dto.responses.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse save(UserRequest userRequest);
    Optional<UserResponse> findByUsernameAndPassword(String username, String password);
    Optional<UserResponse> findByUsername(String username);
}
