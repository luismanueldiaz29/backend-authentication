package com.luis.diaz.authentication.api.services.impl;

import com.google.common.hash.Hashing;
import com.luis.diaz.authentication.api.infraestructure.repositories.UserRepository;
import com.luis.diaz.authentication.api.services.UserService;
import com.luis.diaz.authentication.api.dto.requests.UserRequest;
import com.luis.diaz.authentication.api.dto.responses.UserResponse;
import com.luis.diaz.authentication.api.infraestructure.entities.User;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

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

    @Override
    public UserResponse save(UserRequest userRequest) {
        try {
            User userNew = convertRequestToEntity(userRequest);
            userNew.setPassword(Hashing.sha512().hashString(userRequest.getPassword(), StandardCharsets.UTF_8).toString());
            User userResponse = userRepository.save(userNew);

            return convertEntityToResponse(userResponse);
        }catch (ConstraintViolationException e){
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<UserResponse> findByUsernameAndPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(convertEntityToResponse(user.get()));
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null)
            return Optional.empty();

        return Optional.of(convertEntityToResponse(user));
    }

    @Override
    public Optional<UserResponse> findById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(convertEntityToResponse(user.get()));
    }

    @Override
    public UserResponse delete(long id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return convertEntityToResponse(user);
    }

    @Override
    public UserResponse update(long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow();
        user.setPassword(Hashing.sha512().hashString(userRequest.getPassword(), StandardCharsets.UTF_8).toString());
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getLastName());
        user.setSecondName(userRequest.getSecondName());
        user.setUsername(userRequest.getUsername());

        return convertEntityToResponse(userRepository.update(user));
    }

    private UserResponse convertEntityToResponse(User user){
        return modelMapper.map(user, UserResponse.class);
    }

    private User convertRequestToEntity(UserRequest userRequest){
        return modelMapper.map(userRequest, User.class);
    }
}
