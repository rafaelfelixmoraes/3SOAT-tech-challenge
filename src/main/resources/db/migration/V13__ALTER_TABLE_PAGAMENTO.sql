ALTER TABLE pagamento
    ADD COLUMN id_mercado_pago_qr BIGINT;

ALTER TABLE pagamento
    ADD COLUMN url_qr_code VARCHAR(255);

ALTER TABLE pagamento
    ADD COLUMN status_pagamento VARCHAR(255) CHECK (status_pagamento IN ('RECEBIDO','EM_PREPARACAO','PRONTO','FINALIZADO','CANCELADO'));