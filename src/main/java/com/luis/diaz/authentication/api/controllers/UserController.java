package com.luis.diaz.authentication.api.controllers;

import com.luis.diaz.authentication.api.dto.requests.UserRequest;
import com.luis.diaz.authentication.api.dto.responses.DefaultResponse;
import com.luis.diaz.authentication.api.dto.responses.UserResponse;
import com.luis.diaz.authentication.api.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import javax.validation.Valid;
import java.util.Optional;
import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
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

    @Get("/{id}")
    public HttpResponse<UserResponse> findById(@PathVariable("id") long id){
        Optional<UserResponse> userResponse = userService.findById(id);

        if (userResponse.isEmpty()){
            return HttpResponse.noContent();
        }
        return HttpResponse.ok(userResponse.get());
    }

    @Get("/username/{username}")
    public HttpResponse<UserResponse> findByUsername(@PathVariable("username") String username){
        Optional<UserResponse> userResponse = userService.findByUsername(username);

        if (userResponse.isEmpty()){
            return HttpResponse.noContent();
        }

        return HttpResponse.ok(userResponse.get());
    }

    @Post
    public HttpResponse<UserResponse> save(@Body @Valid UserRequest userRequest){
        UserResponse users = userService.save(userRequest);

        if (users == null){
            return HttpResponse.badRequest();
        }

        return HttpResponse.ok(users);
    }

    @Delete("/{id}")
    public HttpResponse<DefaultResponse<UserResponse>> delete(@PathVariable("id") long id){
        Optional<UserResponse> userResponse = userService.findById(id);
        if (userResponse.isEmpty())
            return HttpResponse.badRequest().body(
                DefaultResponse.<UserResponse>builder()
                    .error(true)
                    .statusCode(HttpStatus.BAD_REQUEST.getCode())
                    .message("El usuario no existe")
                    .body(null)
                    .build()
            );

        UserResponse userResponseDelete = userService.delete(id);
        return HttpResponse.ok(DefaultResponse.<UserResponse>builder()
            .error(false)
            .statusCode(HttpStatus.OK.getCode())
            .message("El usuario fue eliminado")
            .body(userResponseDelete)
            .build());
    }

    @Put("/{id}")
    public HttpResponse<DefaultResponse<UserResponse>> update(@PathVariable("id") long id, @Body @Valid UserRequest userRequest){
        Optional<UserResponse> userResponse = userService.findById(id);
        if (userResponse.isEmpty())
            return HttpResponse.badRequest().body(
                DefaultResponse.<UserResponse>builder()
                    .error(true)
                    .statusCode(HttpStatus.BAD_REQUEST.getCode())
                    .message("El usuario no existe")
                    .body(null)
                    .build()
            );

        UserResponse userResponseDelete = userService.update(id, userRequest);
        return HttpResponse.ok(DefaultResponse.<UserResponse>builder()
            .error(false)
            .statusCode(HttpStatus.OK.getCode())
            .message("El usuario ha sido modificado")
            .body(userResponseDelete)
            .build());
    }

}
