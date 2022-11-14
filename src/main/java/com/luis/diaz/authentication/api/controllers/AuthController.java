package com.luis.diaz.authentication.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.hash.Hashing;
import com.luis.diaz.authentication.api.dto.requests.AuthRequest;
import com.luis.diaz.authentication.api.dto.responses.AuthResponse;
import com.luis.diaz.authentication.api.dto.responses.DefaultResponse;
import com.luis.diaz.authentication.api.services.AuthService;
import com.luis.diaz.authentication.api.services.UserService;
import com.luis.diaz.authentication.api.dto.responses.UserResponse;
import com.nimbusds.jose.JOSEException;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
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

    @Post("/login")
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
            AuthResponse authResponse = authService.login(userResponse.get(), false);
            return HttpResponse.ok(
                    DefaultResponse.<AuthResponse>builder().error(false).body(authResponse).statusCode(HttpStatus.OK.getCode())
                            .message("El usuario inicio sesión correctamente")
                            .build()
            );
        } catch (JOSEException | JsonProcessingException e) {
            log.error(e.getMessage());
            return HttpResponse.badRequest(
                    DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Get("/refresh-token")
    public HttpResponse<DefaultResponse<AuthResponse>> renewToken(HttpHeaders httpHeaders) {
        String authorization = httpHeaders.getAuthorization().orElse(null);
        if(authorization == null){
            return HttpResponse.badRequest(
                DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                        .message("Se debe suministrar el refresh token.")
                        .build()
            );
        }

        String username = null;
        try {
            String token = authorization.substring(7);
            username = authService.getNameFromToken(token);
            if (username == null || username.isEmpty())
                return HttpResponse.badRequest(DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                        .message("El nombre de usuario no puede estar vacío.")
                        .build());
            if(!authService.isTokenRefresh(token)) {
                return HttpResponse.badRequest(DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                        .message("Token suministrado es incorrecto.")
                        .build());
            }
        }catch (ParseException e){
            return HttpResponse.badRequest(
                    DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                            .message("Ocurrió un error al intentar parsear el token. Asegúrese de que sea un token válido.")
                            .build());
        }

        Optional<UserResponse> optionalAppUser = userService.findByUsername(username);
        if (optionalAppUser.isEmpty())
            return HttpResponse.badRequest(DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                .message("Nombre de usuario incorrecto.")
                .build());
        try {
            AuthResponse responseToken = authService.login(optionalAppUser.get(), true);
            return HttpResponse.ok(
                DefaultResponse.<AuthResponse>builder().error(false).body(responseToken).statusCode(HttpStatus.OK.getCode())
                        .message("El token a sido renovado")
                        .build());
        } catch (JsonProcessingException | JOSEException e) {
            return HttpResponse.badRequest(
                DefaultResponse.<AuthResponse>builder().error(true).body(null).statusCode(HttpStatus.BAD_REQUEST.getCode())
                        .message("Ocurrió una excepción: "+ e.getMessage())
                        .build());
        }
    }

}
