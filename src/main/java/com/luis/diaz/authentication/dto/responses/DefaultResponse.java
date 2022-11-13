package com.luis.diaz.authentication.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponse<T>{
    private boolean error;
    private int statusCode;
    private String message;
    private T body;
}
