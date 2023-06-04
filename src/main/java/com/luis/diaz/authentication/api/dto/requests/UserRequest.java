package com.luis.diaz.authentication.api.dto.requests;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Introspected
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull(message = "El usuario es requerido")
    @NotBlank(message = "El usuario se le debe asignar un valor")
    private String username;

    @NotNull(message = "La contraseña es requerido")
    @NotBlank(message = "A la contraseña se le debe asignar un valor")
    private String password;

    @NotNull(message = "El tipo de usuario es requerido")
    @NotBlank(message = "El tipo de usuario se le debe asignar un valor")
    private String userType;

    @NotNull(message = "El correo del usuario es requerido")
    @NotBlank(message = "El correo del usuario se le debe asignar un valor")
    private String email;

    @NotNull(message = "El nombre es requerido")
    @NotBlank(message = "El nombre se le debe asignar un valor")
    private String name;

    @NotNull(message = "El apellido es requerido")
    @NotBlank(message = "El apellido se le debe asignar un valor")
    private String lastname;

    @Nullable
    private String position;

    @Nullable
    private String phone;
}
