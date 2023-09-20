CREATE TABLE limite_diario (
   id SERIAL NOT NULL,
   agencia INT NOT NULL,
   conta INT NOT NULL,
   data DATE NOT NULL,
   valor DECIMAL(8,2),
   PRIMARY KEY (id)
);
