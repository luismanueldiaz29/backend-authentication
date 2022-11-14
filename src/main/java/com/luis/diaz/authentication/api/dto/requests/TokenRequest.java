package com.luis.diaz.authentication.api.dto.requests;

import lombok.Data;

@Data
public class TokenRequest<T> {
    private T data;
}
