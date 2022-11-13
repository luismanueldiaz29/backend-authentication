package com.luis.diaz.authentication.controllers;

import com.luis.diaz.authentication.dto.responses.UserResponse;
import com.luis.diaz.authentication.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/user")
@Tag(name = "User")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Get
    public HttpResponse<List<UserResponse>> findAll(){
        List<UserResponse> users = userService.findAll();

        if (users.isEmpty()){
            return HttpResponse.noContent();
        }

        return HttpResponse.ok(users);
    }
}
