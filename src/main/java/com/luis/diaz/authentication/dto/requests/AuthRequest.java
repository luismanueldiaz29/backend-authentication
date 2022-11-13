package com.luis.diaz.authentication.dto.requests;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
