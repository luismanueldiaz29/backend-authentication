package com.luis.diaz.authentication.dto;

import lombok.Data;

@Data
public class UserTokenDto<T> {
    private T data;
}
