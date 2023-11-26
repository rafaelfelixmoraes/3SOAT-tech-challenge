CREATE TABLE usuario (
    id BIGINT NOT NULL,
    usuario VARCHAR(255),
    senha VARCHAR(255),
    role VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE SEQUENCE usuario_seq START WITH 1 INCREMENT BY 1;
