package com.luis.diaz.authentication.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luis.diaz.authentication.dto.requests.AuthRequest;
import com.luis.diaz.authentication.dto.responses.TokenResponse;
import com.nimbusds.jose.JOSEException;

import java.time.DateTimeException;

public interface AuthService {
    TokenResponse login(AuthRequest authRequest) throws JOSEException, DateTimeException, JsonProcessingException;
}
