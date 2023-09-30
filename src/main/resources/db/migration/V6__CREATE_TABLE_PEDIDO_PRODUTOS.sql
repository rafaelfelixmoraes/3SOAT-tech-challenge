CREATE TABLE pedido_produtos (
    pedido_id bigint NOT NULL,
    produtos_id bigint NOT NULL,
    PRIMARY KEY (pedido_id, produtos_id)
);