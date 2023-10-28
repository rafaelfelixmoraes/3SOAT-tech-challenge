INSERT INTO cliente (id, nome, cpf, email)
VALUES (10, 'Ana Maria', '603.072.360-05', 'ana.maria@gmail.com');

INSERT INTO produto (valor_unitario, categoria_id, id, descricao)
VALUES (5.00, 2, 10, 'Coca Cola');

INSERT INTO produto (valor_unitario, categoria_id, id, descricao)
VALUES (5.00, 2, 15, 'Pepsi');

INSERT INTO pedido (senha_retirada, cliente_id, valor_total, status_pedido, id)
VALUES (12348, 10, 42.50, 'EM_PREPARACAO', 200);

INSERT INTO pedido_produtos (pedido_id, produtos_id)
VALUES (200, 15);

INSERT INTO pedido_produtos (pedido_id, produtos_id)
VALUES (100, 10);