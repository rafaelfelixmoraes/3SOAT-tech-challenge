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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClienteServiceTest {

    private ClienteService clienteService;

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

    private Cliente setCliente() {
        return Cliente.builder()
                .id(10L)
                .nome("Ana Maria")
                .email("ana.maria@gmail.com")
                .cpf("603.072.360-05")
                .build();
    }

}
