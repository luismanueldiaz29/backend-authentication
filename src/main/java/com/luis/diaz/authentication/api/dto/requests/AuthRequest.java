package com.luis.diaz.authentication.api.dto.requests;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
