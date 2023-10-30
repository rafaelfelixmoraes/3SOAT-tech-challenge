CREATE OR REPLACE VIEW View_FilaPedidos
AS

SELECT
pd.senha_retirada AS senha,
cli.nome AS nomeCliente,
pd.status_pedido AS statusPedido
FROM pedido pd
JOIN cliente cli ON cli.id = pd.cliente_id
WHERE status_pedido NOT IN ('FINALIZADO', 'CANCELADO')