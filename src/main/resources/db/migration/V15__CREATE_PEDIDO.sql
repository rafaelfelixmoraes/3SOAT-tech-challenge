-- Criando o pedido e associe o produto
INSERT INTO Pedido (senha_retirada, cliente_id, valor_total, status_pedido, id)
VALUES (12348, 100, 42.50, 'EM_PREPARACAO', 100);

-- Associe o produto criado ao pedido
INSERT INTO Pedido_Produtos (pedido_id, produtos_id)
VALUES (100, 100);

INSERT INTO Pedido_Produtos (pedido_id, produtos_id)
VALUES (100, 101);