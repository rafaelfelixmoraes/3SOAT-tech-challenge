INSERT INTO cliente (id, nome, cpf, email)
VALUES (10, 'Ana Maria', '603.072.360-05', 'ana.maria@gmail.com');

INSERT INTO produto (valor_unitario, categoria_id, id, descricao)
VALUES (5.00, 2, 10, 'Coca Cola');

INSERT INTO produto (valor_unitario, categoria_id, id, descricao)
VALUES (5.00, 2, 15, 'Pepsi');

INSERT INTO public.pedido(
    senha_retirada, valor_total, cliente_id, id, status_pedido, data_hora)
VALUES (696328, 5.00, 10, 10, 'RECEBIDO', '2023-10-26 22:10:07.949088');

INSERT INTO public.pagamento (id, data_hora_pagamento, valor_total, pedido_id, qr_data, status_pagamento)
VALUES (10, '2023-10-26 22:10:12.640283', 5.00, 10, NULL, 'AGUARDANDO_PAGAMENTO');