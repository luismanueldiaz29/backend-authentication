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

-- USER -> admin@admin PASS -> 123
INSERT INTO users(NAME, SECOND_NAME, LAST_NAME, USERNAME, PASSWORD)
VALUES ('admin', null, 'admin', 'admin@admin','d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db');

----------------------------------------------

create table usuarios
(
    usuario_id   bigserial,
    usuario      varchar(255) not null,
    clave        varchar(255) not null,
    tipo_usuario varchar(50)  not null,
    email        varchar(255)
);

insert into usuarios ( usuario, clave, tipo_usuario, email)
values ('admin@admin','d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db','CLIENTE','admin@gmail.com')