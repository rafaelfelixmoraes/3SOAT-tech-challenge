package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.FilaPedidosRepository;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import br.com.tech.challenge.domain.enums.StatusPedido;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FilaPedidosServiceTest {

    private final FilaPedidosService filaPedidosService;

    @Mock
    private FilaPedidosRepository filaPedidosRepository;

    FilaPedidosServiceTest() {
        MockitoAnnotations.openMocks(this);
        filaPedidosService = new FilaPedidosService(filaPedidosRepository);
    }

    @DisplayName("Deve listar a fila de pedidos com sucesso")
    @Test
    void shouldListFilaPedidosSuccess() {
        var listaPedidos = new PageImpl<>(setFilaPedidos());

        when(filaPedidosRepository.findAll(any(PageRequest.class))).thenReturn(listaPedidos);

        var listaPedidosReturned = filaPedidosService.listFilaPedidos(0, 10);

        assertNotNull(listaPedidosReturned);
        assertFalse(listaPedidosReturned.isEmpty());
        assertEquals(1L, listaPedidosReturned.getTotalElements());
    }

    @DisplayName("Deve listar a fila de pedidos vazia com sucesso")
    @Test
    void shouldListEmptyFilaPedidos() {
        when(filaPedidosRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        var listaPedidosReturned = filaPedidosService.listFilaPedidos(0, 10);

        assertNotNull(listaPedidosReturned);
        assertTrue(listaPedidosReturned.isEmpty());
        assertEquals(0L, listaPedidosReturned.getTotalElements());
    }

    private List<FilaPedidos> setFilaPedidos() {
        var filaPedidos = FilaPedidos.builder()
                .senhaRetirada(RandomUtils.nextInt())
                .idCliente(1)
                .statusPedido(StatusPedido.FINALIZADO.getDescricao())
                .build();

        return Collections.singletonList(filaPedidos);
    }

}
