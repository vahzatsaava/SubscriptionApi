CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    name     VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    role     VARCHAR(255),
    status   varchar(255)
);
