package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.FilaPedidosRepository;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import br.com.tech.challenge.domain.enums.StatusPedido;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
class FilaPedidosServiceTest {

    @InjectMocks
    private FilaPedidosService filaPedidosService;

    @Mock
    private FilaPedidosRepository filaPedidosRepository;

    @DisplayName("Deve listar a fila de pedidos com sucesso")
    @Test
    void listFilaPedidos(){
        var listaPedidos = setFilaPedidos();

        when(filaPedidosRepository.findAll()).thenReturn(listaPedidos);

        var listaPedidosReturned = filaPedidosService.listaFilaPedidos();

        assertNotNull(listaPedidosReturned);
        assertFalse(listaPedidosReturned.isEmpty());
        assertEquals(listaPedidos.get(0).getSenhaRetirada(), listaPedidosReturned.get(0).getSenhaRetirada());
        assertEquals(listaPedidos.get(0).getNomeCliente(), listaPedidosReturned.get(0).getNomeCliente());
        assertEquals(listaPedidos.get(0).getStatusPedido(), listaPedidosReturned.get(0).getStatusPedido());
    }

    private List<FilaPedidos> setFilaPedidos() {
        var filaPedidos = FilaPedidos.builder()
                .senhaRetirada(RandomUtils.nextInt())
                .nomeCliente("Teste Fila Pedidos")
                .statusPedido(StatusPedido.FINALIZADO.getDescricao())
                .build();

        return Collections.singletonList(filaPedidos);
    }
}
