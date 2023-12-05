package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ClienteAlreadyExistsException;
import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.entidades.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ClienteServiceTest {

    private final ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ModelMapper mapper;

    ClienteServiceTest() {
        MockitoAnnotations.openMocks(this);
        clienteService = new ClienteService(clienteRepository, mapper);
    }

    @DisplayName("Deve criar um cliente com sucesso")
    @Test
    void shouldCreateClienteSuccess() {

        Cliente clienteSalvo = this.setCliente();

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCpf(clienteSalvo.getCpf());

        when(mapper.map(clienteDTO, Cliente.class)).thenReturn(clienteSalvo);
        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente clienteRetornado = clienteService.save(clienteDTO);

        assertNotNull(clienteRetornado);
        assertEquals(10L, clienteRetornado.getId());
    }

    @DisplayName("Deve retornar exceção ao tentar criar um cliente que já possui cadastro")
    @Test
    void shouldThrowExceptionWhenUsingSaveMethod() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCpf("927.965.620-18");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(2L);

        when(clienteRepository.findByCpf("927.965.620-18")).thenReturn(Optional.of(clienteExistente));

        assertThrows(ClienteAlreadyExistsException.class, () -> {
            clienteService.save(clienteDTO);
        });
    }


    @DisplayName("Deve criar um cliente com sucesso somente com cpf")
    @Test
    void shouldSaveClientWithCpfOnly() {
        String cpf = "037.952.160-10";

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(3L);

        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente clienteRetornado = clienteService.saveClientWithCpf(cpf);

        assertNotNull(clienteRetornado);
        assertEquals(3L, clienteRetornado.getId());
    }

    @DisplayName("Deve retornar exceção ao tentar criar um cliente que já possui cadastro")
    @Test
    void shouldThrowExceptionWhenUsingSaveClientWithCpfMethod() {
        final String cpf = "634.964.890-06";

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(4L);

        when(clienteRepository.findByCpf("634.964.890-06")).thenReturn(Optional.of(clienteExistente));

        assertThrows(ClienteAlreadyExistsException.class, () -> {
            clienteService.saveClientWithCpf(cpf);
        });
    }

    @DisplayName("Deve listar clientes com sucesso")
    @Test
    void shouldListClientSuccess() {
        var listClientes = new PageImpl<>(setListCliente());

        when(clienteRepository.findAll(any(Pageable.class))).thenReturn(listClientes);

        var clientes = clienteService.list(null, anyInt(), 10);

        var listClientesContent = clientes.getContent();

        for (int i = 0; i < listClientesContent.size(); i++) {
            assertEquals(listClientesContent.get(i).getId(), clientes.getContent().get(i).getId());
            assertEquals(listClientesContent.get(i).getNome(), clientes.getContent().get(i).getNome());
            assertEquals(listClientesContent.get(i).getEmail(), clientes.getContent().get(i).getEmail());
            assertEquals(listClientesContent.get(i).getCpf(), clientes.getContent().get(i).getCpf());
        }
    }


    @DisplayName("Deve listar o cliente por id com sucesso")
    @Test
    void shouldListClientByIdSuccess() {
        var listClientes = new PageImpl<>(setListCliente());

        when(clienteRepository.findById(eq(10L),any(Pageable.class))).thenReturn(listClientes);

        var clientes = clienteService.list(10L, 0, 10);

        var listClientesContent = clientes.getContent();

        for (int i = 0; i < listClientesContent.size(); i++) {
            assertEquals(listClientesContent.get(i).getId(), clientes.getContent().get(i).getId());
            assertEquals(listClientesContent.get(i).getNome(), clientes.getContent().get(i).getNome());
            assertEquals(listClientesContent.get(i).getEmail(), clientes.getContent().get(i).getEmail());
            assertEquals(listClientesContent.get(i).getCpf(), clientes.getContent().get(i).getCpf());
        }
    }

    @DisplayName("Deve existir um cliente com o id informado")
    @Test
    void shouldFindClienteById() {
        final Long id = 10L;

        when(clienteService.existsById(id)).thenReturn(true);
        var exists = clienteService.existsById(id);

        assertTrue(exists);
    }

    @DisplayName("Não deve existir um cliente com o id informado")
    @Test
    void shouldNotFindClienteById() {
        final Long id = 11L;

        when(clienteService.existsById(id)).thenReturn(false);
        var exists = clienteService.existsById(id);

        assertFalse(exists);
    }

    private Cliente setCliente() {
        return Cliente.builder()
                .id(10L)
                .nome("Ana Maria")
                .email("ana.maria@gmail.com")
                .cpf("603.072.360-05")
                .build();
    }

    private List<Cliente> setListCliente() {
        var cliente = Cliente.builder()
                .id(10L)
                .nome("Ana Maria")
                .email("ana.maria@gmail.com")
                .cpf("603.072.360-05")
                .build();
        return Collections.singletonList(cliente);
    }

}
