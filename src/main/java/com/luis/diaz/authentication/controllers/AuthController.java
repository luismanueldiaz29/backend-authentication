package com.luis.diaz.authentication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.hash.Hashing;
import com.luis.diaz.authentication.dto.requests.AuthRequest;
import com.luis.diaz.authentication.dto.responses.AuthResponse;
import com.luis.diaz.authentication.dto.responses.DefaultResponse;
import com.luis.diaz.authentication.dto.responses.UserResponse;
import com.luis.diaz.authentication.services.AuthService;
import com.luis.diaz.authentication.services.UserService;
import com.nimbusds.jose.JOSEException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Tag(name = "Auth")
@Controller("/api/auth")
@ExecuteOn(TaskExecutors.IO)
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Post
    public HttpResponse<DefaultResponse<AuthResponse>> login(@Body AuthRequest authRequest){
        String passwordEncrypt = Hashing.sha512().hashString(authRequest.getPassword(), StandardCharsets.UTF_8).toString();
        Optional<UserResponse> userResponse = userService.findByUsernameAndPassword(authRequest.getUsername(), passwordEncrypt);

        if(userResponse.isEmpty()){
            return HttpResponse.badRequest(
                    DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                            .message("Credenciales incorrectas")
                    .build()
            );
        }

        try {
            AuthResponse authResponse = authService.login(userResponse.get());
            return HttpResponse.badRequest(
                    DefaultResponse.<AuthResponse>builder()
                            .error(false)
                            .body(authResponse)
                            .statusCode(HttpStatus.OK.getCode())
                            .message("El usuario inicio sesión correctamente")
                            .build()
            );
        } catch (JOSEException | JsonProcessingException e) {
            log.error(e.getMessage());
            return HttpResponse.badRequest(
                    DefaultResponse.<AuthResponse>builder().error(true).body(null)
                            .statusCode(HttpStatus.BAD_REQUEST.getCode())
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
