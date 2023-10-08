package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ClienteAlreadyExistsException;
import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.domain.dto.ClienteCpfDTO;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.entidades.Cliente;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final ModelMapper mapper;

    @Transactional
    public Cliente save(ClienteDTO clienteDTO) {

        isClienteAlreadyExists(clienteDTO.getCpf());
        return clienteRepository.save(mapper.map(clienteDTO, Cliente.class));
    }

    public Cliente saveClientWithCpf(String cpf) {

        isClienteAlreadyExists(cpf);
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);

        return clienteRepository.save(cliente);
    }

    public Cliente findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    private void isClienteAlreadyExists(String cpf) {
        Cliente clienteAlreadyExists = this.findByCpf(cpf);
        if (clienteAlreadyExists != null) {
            throw new ClienteAlreadyExistsException("Cliente já cadastrado");
        }
    }

}
