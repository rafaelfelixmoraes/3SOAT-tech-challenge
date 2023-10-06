ALTER TABLE pedido
ALTER COLUMN cliente_id SET NOT NULL;

ALTER TABLE pedido
DROP CONSTRAINT pedido_cliente_id_key;