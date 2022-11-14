package com.luis.diaz.authentication.api.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayloadTokenRequest {
    private long id;
    private String name;
    private String secondName;
    private String lastName;
    private String username;
}
