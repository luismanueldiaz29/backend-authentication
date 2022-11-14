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

    /**
     * > It takes a list of users from the database, converts them to a list of user responses, and returns the list of
     * user responses
     *
     * @return A list of UserResponse objects.
     */
    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::convertEntityToResponse).toList();
    }

    /**
     * It saves a user to the database.
     *
     * @param userRequest The request object that contains the user information.
     * @return UserResponse
     */
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

    /**
     * > If the user is not found, return an empty optional. Otherwise, return an optional containing the user
     *
     * @param username The username of the user you want to find.
     * @param password The password to use for the new account.
     * @return Optional<UserResponse>
     */
    @Override
    public Optional<UserResponse> findByUsernameAndPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(convertEntityToResponse(user.get()));
    }

    /**
     * > If the user is not found, return an empty optional. Otherwise, return an optional containing the user
     *
     * @param username The username of the user you want to find.
     * @return Optional<UserResponse>
     */
    @Override
    public Optional<UserResponse> findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null)
            return Optional.empty();

        return Optional.of(convertEntityToResponse(user));
    }

    /**
     * > If the user is not found, return an empty optional. Otherwise, return an optional containing the user
     *
     * @param id The id of the user you want to find.
     * @return Optional<UserResponse>
     */
    @Override
    public Optional<UserResponse> findById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
    /**
     * > Delete a user from the database
     *
     * @param id The id of the user to be deleted.
     * @return UserResponse
     */
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

    /**
     * It updates the user's password, name, last name, second name and username.
     *
     * @param id The id of the user to be updated.
     * @param userRequest the request object that contains the data that will be used to update the user.
     * @return UserResponse
     */
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

    /**
     * Convert the User entity to a UserResponse object using the modelMapper object.
     *
     * @param user The user object that we want to convert.
     * @return A UserResponse object
     */
    private UserResponse convertEntityToResponse(User user){
        return modelMapper.map(user, UserResponse.class);
    }

    /**
     * It takes a UserRequest object and converts it to a User object
     *
     * @param userRequest The request object that is passed to the controller.
     * @return A User object
     */
    private User convertRequestToEntity(UserRequest userRequest){
        return modelMapper.map(userRequest, User.class);
    }
}
