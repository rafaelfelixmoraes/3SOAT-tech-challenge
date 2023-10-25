ALTER TABLE pagamento
    ADD COLUMN qr_data VARCHAR(255);

ALTER TABLE pagamento
    ADD COLUMN status_pagamento VARCHAR(255) CHECK (status_pagamento IN ('AGUARDANDO_PAGAMENTO','PAGO'));