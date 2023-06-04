package com.luis.diaz.authentication.api.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String username;
    private String userType;
    private String email;
    private String name;
    private String lastname;
    private String position;
    private String phone;
}
