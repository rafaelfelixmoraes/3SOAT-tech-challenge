package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ClienteAlreadyExistsException;
import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.entidades.Cliente;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final ModelMapper mapper;

    @Transactional
    public Cliente save(ClienteDTO clienteDTO) {

        validateIfClientExistsByCpf(clienteDTO.getCpf());
        return clienteRepository.save(mapper.map(clienteDTO, Cliente.class));
    }

    public Cliente saveClientWithCpf(String cpf) {

        validateIfClientExistsByCpf(cpf);
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);

        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    private void validateIfClientExistsByCpf(String cpf) {
        Optional<Cliente> clienteAlreadyExists = this.findByCpf(cpf);
        if (clienteAlreadyExists.isPresent()) {
            throw new ClienteAlreadyExistsException("Cliente já cadastrado");
        }
    }

}
