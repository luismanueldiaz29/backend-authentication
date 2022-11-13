create table users
(
    id          bigserial not null
        constraint users_pk
            primary key,
    name        varchar(20)                                     not null,
    second_name varchar(20),
    last_name   varchar(20)                                     not null,
    username    varchar(200)                                    not null,
    password    varchar(200)                                    not null
);