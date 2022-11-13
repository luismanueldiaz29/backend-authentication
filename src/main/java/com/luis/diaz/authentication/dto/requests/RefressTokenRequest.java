package com.luis.diaz.authentication.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefressTokenRequest {
    private Long id;
    private String login;
    private boolean status;
}
