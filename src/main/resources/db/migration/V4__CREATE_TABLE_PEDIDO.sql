CREATE TABLE pedido (
    senha_retirada INTEGER,
    valor_total NUMERIC(38,2),
    cliente_id bigint UNIQUE,
    id bigint NOT NULL,
    status_pedido VARCHAR(255) CHECK (status_pedido IN ('RECEBIDO','EM_PREPARACAO','PRONTO','FINALIZADO','CANCELADO')),
    PRIMARY KEY (id)
);