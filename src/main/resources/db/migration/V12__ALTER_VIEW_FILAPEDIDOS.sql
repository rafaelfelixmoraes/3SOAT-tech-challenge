DROP VIEW View_FilaPedidos;

CREATE OR REPLACE VIEW View_FilaPedidos
AS

SELECT
    pd.senha_retirada AS senha,
    cli.id AS id_cliente,
    pd.status_pedido AS status_pedido
FROM pedido pd
         JOIN cliente cli ON cli.id = pd.cliente_id
WHERE status_pedido NOT IN ('FINALIZADO', 'CANCELADO')