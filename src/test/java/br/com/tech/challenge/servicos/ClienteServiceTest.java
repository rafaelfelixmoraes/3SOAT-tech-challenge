package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.entidades.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ModelMapper mapper;

    @DisplayName("Deve criar um produto com sucesso")
    @Test
    void createClienteSuccess() {
        when(clienteRepository.save(any())).thenReturn(cliente());

        var cliente = clienteService.save(mapper.map(cliente(), ClienteDTO.class));

        var returnedCliente = cliente();

        assertEquals(returnedCliente.getId(), cliente.getId());
        assertEquals(returnedCliente.getNome(), cliente.getNome());
        assertEquals(returnedCliente.getEmail(), cliente.getEmail());
        assertEquals(returnedCliente.getCpf(), cliente.getCpf());
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
