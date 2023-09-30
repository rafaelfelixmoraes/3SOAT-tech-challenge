CREATE TABLE produto (
    valor_unitario NUMERIC(38,2),
    categoria_id bigint NOT NULL,
    id bigint NOT NULL,
    descricao VARCHAR(100),
    PRIMARY KEY (id)
);