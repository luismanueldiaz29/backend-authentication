package com.luis.diaz.authentication.api.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayloadTokenRequest {
    private long id;
    private String username;
//    private String password;
    private String userType;
    private String email;
}
