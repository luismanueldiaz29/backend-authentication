package com.luis.diaz.authentication.api.dto.requests;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Introspected
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull(message = "El nombre del usuario es requerido")
    @NotBlank(message = "Al nombre del usuario se le debe asignar un valor")
    private String name;

    @Nullable
    private String secondName;

    @NotNull(message = "El apellido del usuario es requerido")
    @NotBlank(message = "Al apellido del usuario se le debe asignar un valor")
    private String lastName;

    @NotNull(message = "El usuario es requerido")
    @NotBlank(message = "El usuario se le debe asignar un valor")
    private String username;

    @NotNull(message = "La contraseña es requerido")
    @NotBlank(message = "A la contraseña se le debe asignar un valor")
    private String password;

}
