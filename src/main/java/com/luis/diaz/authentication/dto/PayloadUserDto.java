package com.luis.diaz.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayloadUserDto {
    private long id;
    private String name;
    private String secondName;
    private String lastName;
    private String username;
}
