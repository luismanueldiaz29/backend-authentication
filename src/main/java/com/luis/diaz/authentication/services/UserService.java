package com.luis.diaz.authentication.services;

import com.luis.diaz.authentication.dto.requests.UserRequest;
import com.luis.diaz.authentication.dto.responses.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse save(UserRequest userRequest);
}
