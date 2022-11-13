package com.luis.diaz.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserDto {
    private Long id;
    private String login;
    private boolean status;
}
