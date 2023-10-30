CREATE TABLE produto (
    valor_unitario NUMERIC(38,2),
    categoria_id BIGINT NOT NULL,
    id BIGINT NOT NULL,
    descricao VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE SEQUENCE produto_seq START WITH 1 INCREMENT BY 1;

ALTER TABLE IF EXISTS produto ADD CONSTRAINT FKopu9jggwnamfv0c8k2ri3kx0a FOREIGN KEY (categoria_id) REFERENCES categoria;