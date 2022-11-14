package com.luis.diaz.authentication.services.impl;

import com.google.common.hash.Hashing;
import com.luis.diaz.authentication.dto.requests.UserRequest;
import com.luis.diaz.authentication.dto.responses.UserResponse;
import com.luis.diaz.authentication.infraestructure.entities.User;
import com.luis.diaz.authentication.infraestructure.repositories.UserRepository;
import com.luis.diaz.authentication.services.UserService;
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

    private UserResponse convertEntityToResponse(User user){
        return modelMapper.map(user, UserResponse.class);
    }

    private User convertRequestToEntity(UserRequest userRequest){
        return modelMapper.map(userRequest, User.class);
    }
}
