package com.luis.diaz.authentication.dto;

import lombok.Data;

@Data
public class AppUserDto {
    public AppUserDto(){}

    private Long id;

    private String login;

    private boolean status;
}
