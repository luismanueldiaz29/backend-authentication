package com.luis.diaz.authentication.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luis.diaz.authentication.dto.responses.AuthResponse;
import com.luis.diaz.authentication.dto.responses.UserResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.time.DateTimeException;

public interface AuthService {
    AuthResponse login(UserResponse userResponse, boolean isRenew) throws JOSEException, DateTimeException, JsonProcessingException;
    String getNameFromToken(String token) throws ParseException;
    boolean isTokenRefresh(String token) throws ParseException;
}
