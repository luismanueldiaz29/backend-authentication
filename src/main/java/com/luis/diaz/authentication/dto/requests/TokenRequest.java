package com.luis.diaz.authentication.dto.requests;

import lombok.Data;

@Data
public class TokenRequest<T> {
    private T data;
}
