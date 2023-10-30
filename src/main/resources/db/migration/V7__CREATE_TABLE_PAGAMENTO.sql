CREATE TABLE pagamento (
    id BIGINT NOT NULL,
    data_hora_pagamento TIMESTAMP(6),
    valor_total NUMERIC(38,2),
    pedido_id BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS pagamento DROP CONSTRAINT IF EXISTS UK_sc46s3wc046ujpdoumidm4cr7;

ALTER TABLE IF EXISTS pagamento ADD CONSTRAINT UK_sc46s3wc046ujpdoumidm4cr7 UNIQUE (pedido_id);

CREATE SEQUENCE pagamento_seq START WITH 1 INCREMENT BY 1;

ALTER TABLE IF EXISTS pagamento ADD CONSTRAINT FKthad9tkw4188hb3qo1lm5ueb0 FOREIGN KEY (pedido_id) REFERENCES pedido;