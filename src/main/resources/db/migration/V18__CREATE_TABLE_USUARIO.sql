CREATE TABLE usuario (
    id BIGINT NOT NULL,
    nome VARCHAR(200),
    cpf VARCHAR(255),
    email VARCHAR(255),
    usuario VARCHAR(255),
    senha VARCHAR(255),
    role VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE SEQUENCE usuario_seq START WITH 1 INCREMENT BY 1;
