package com.luis.diaz.authentication.services.impl;

import com.luis.diaz.authentication.dto.responses.UserResponse;
import com.luis.diaz.authentication.infraestructure.entities.User;
import com.luis.diaz.authentication.infraestructure.repositories.UserRepository;
import com.luis.diaz.authentication.services.UserService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.List;

@Slf4j
@Singleton
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        modelMapper = new ModelMapper();
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::convertEntityToResponse).toList();
    }

    private UserResponse convertEntityToResponse(User user){
        return modelMapper.map(user, UserResponse.class);
    }
}
