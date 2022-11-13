package com.luis.diaz.authentication.infraestructure.entities;

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
@MappedEntity(value = "users")
public class User {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    @MappedProperty(value = "id")
    private long id;

    @MappedProperty(value = "name")
    @NotNull(message = "El nombre del usuario es requerido")
    @NotBlank(message = "Al nombre del usuario se le debe asignar un valor")
    private String name;

    @MappedProperty(value = "second_name")
    private String secondName;

    @MappedProperty(value = "last_name")
    @NotNull(message = "El apellido del usuario es requerido")
    @NotBlank(message = "Al apellido del usuario se le debe asignar un valor")
    private String lastName;

    @MappedProperty(value = "username")
    @NotNull(message = "El usuario es requerido")
    @NotBlank(message = "El usuario se le debe asignar un valor")
    private String username;

    @MappedProperty(value = "password")
    @NotNull(message = "La contraseña es requerido")
    @NotBlank(message = "A la contraseña se le debe asignar un valor")
    private String password;
}
