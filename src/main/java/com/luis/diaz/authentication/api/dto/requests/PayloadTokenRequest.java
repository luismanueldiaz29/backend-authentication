package com.luis.diaz.authentication.api.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayloadTokenRequest {
    private long id;
    private String username;
    private String userType;
    private String email;
    private String name;
    private String lastname;
    private String position;
    private String phone;
}
