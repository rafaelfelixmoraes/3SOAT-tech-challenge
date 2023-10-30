ALTER TABLE pagamento
    ADD COLUMN qr_data VARCHAR(255);

ALTER TABLE pagamento
    ADD COLUMN status_pagamento VARCHAR(255) CHECK (status_pagamento IN ('AGUARDANDO_PAGAMENTO','PAGO'));

INSERT INTO public.pagamento (id, data_hora_pagamento, valor_total, pedido_id, qr_data, status_pagamento)
    VALUES (10, '2023-10-26 22:10:12.640283', 5.00, 100, NULL, 'AGUARDANDO_PAGAMENTO');