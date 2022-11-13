package com.luis.diaz.authentication.infraestructure.repositories;

import com.luis.diaz.authentication.infraestructure.entities.User;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    Optional<User> findByUsernameAndPassword(String username, String password);
}
