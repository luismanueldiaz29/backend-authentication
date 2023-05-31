package com.luis.diaz.authentication.api.infraestructure.entities;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity(value = "usuarios")
public class User {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    @MappedProperty(value = "usuario_id")
    private long id;

    @MappedProperty(value = "usuario")
    @NotNull(message = "El usuario es requerido")
    @NotBlank(message = "El usuario se le debe asignar un valor")
    private String username;

    @MappedProperty(value = "clave")
    @NotNull(message = "La contraseña es requerido")
    @NotBlank(message = "A la contraseña se le debe asignar un valor")
    private String password;

    @MappedProperty(value = "tipo_usuario")
    private String userType;

    @MappedProperty(value = "email")
    @NotNull(message = "El correo del usuario es requerido")
    @NotBlank(message = "El correo del usuario se le debe asignar un valor")
    private String email;
}
