package com.luis.diaz.authentication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luis.diaz.authentication.dto.requests.AuthRequest;
import com.luis.diaz.authentication.dto.responses.TokenResponse;
import com.luis.diaz.authentication.services.AuthService;
import com.nimbusds.jose.JOSEException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.tags.Tag;

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/auth")
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Post
    public HttpResponse<TokenResponse> login(@Body AuthRequest authRequest){
        try {
            return HttpResponse.ok(authService.login(authRequest));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
