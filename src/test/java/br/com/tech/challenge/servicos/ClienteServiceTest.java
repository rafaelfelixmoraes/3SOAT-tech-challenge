package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.entidades.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.tech.challenge.api.exception.ClienteAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ModelMapper mapper;

    @DisplayName("Deve criar um cliente com sucesso")
    @Test
    void createClienteSuccess() {

        Cliente clienteSalvo = this.cliente();

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCpf(clienteSalvo.getCpf());

        when(mapper.map(clienteDTO, Cliente.class)).thenReturn(clienteSalvo);
        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente clienteRetornado = clienteService.save(clienteDTO);

        assertNotNull(clienteRetornado);
        assertEquals(1L, clienteRetornado.getId());
    }

    @DisplayName("Deve retornar Execption ao tentar criar um cliente que ja possui cadastro")
    @Test
    public void testSaveClienteWithExistingCpf() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCpf("927.965.620-18");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(2L);

        when(clienteRepository.findByCpf("927.965.620-18")).thenReturn(clienteExistente);

        assertThrows(ClienteAlreadyExistsException.class, () -> {
            clienteService.save(clienteDTO);
        });
    }


    @DisplayName("Deve criar um cliente com sucesso somente com cpf")
    @Test
    public void testSaveClientWithCpf() {
        String cpf = "037.952.160-10";

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(3L);

        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente clienteRetornado = clienteService.saveClientWithCpf(cpf);

        assertNotNull(clienteRetornado);
        assertEquals(3L, clienteRetornado.getId());
    }

    @DisplayName("Deve retornar Execption ao tentar criar um cliente que ja possui cadastro")
    @Test
    public void testSaveClientWithCpfWithExistingCpf() {
        String cpf = "634.964.890-06";

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(4L);

        when(clienteRepository.findByCpf("634.964.890-06")).thenReturn(clienteExistente);

        assertThrows(ClienteAlreadyExistsException.class, () -> {
            clienteService.saveClientWithCpf(cpf);
        });
    }


    private Cliente cliente() {
        return Cliente.builder()
                .id(1L)
                .nome("Anthony Samuel Joaquim Teixeira")
                .email("anthony.samuel.teixeira@said.adv.br")
                .cpf("143.025.400-95")
                .build();
    }

}
