CREATE TABLE pedido (
    senha_retirada INTEGER,
    valor_total NUMERIC(38,2),
    cliente_id BIGINT UNIQUE,
    id BIGINT NOT NULL,
    status_pedido VARCHAR(255) CHECK (status_pedido IN ('RECEBIDO','EM_PREPARACAO','PRONTO','FINALIZADO','CANCELADO')),
    PRIMARY KEY (id)
);

CREATE SEQUENCE pedido_seq START WITH 1 INCREMENT BY 1;

ALTER TABLE IF EXISTS pedido ADD CONSTRAINT FK30s8j2ktpay6of18lbyqn3632 FOREIGN KEY (cliente_id) REFERENCES cliente;