CREATE TABLE pedido_produtos (
    pedido_id BIGINT NOT NULL,
    produtos_id BIGINT NOT NULL,
    PRIMARY KEY (pedido_id, produtos_id)
);

ALTER TABLE IF EXISTS pedido_produtos ADD CONSTRAINT FKjbeb9e1fpew7efynuggyfyk28 FOREIGN KEY (produtos_id) REFERENCES produto;

ALTER TABLE IF EXISTS pedido_produtos ADD CONSTRAINT FKjyqoxfuf4ce53ggut07np0rsb FOREIGN KEY (pedido_id) REFERENCES pedido;