package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ClienteAlreadyExistsException;
import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.domain.dto.ClienteCheckInDTO;
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

        isClienteAlreadyExists(clienteDTO.getCpf());
        return clienteRepository.save(mapper.map(clienteDTO, Cliente.class));
    }

    public Cliente saveClientWithCpf(String cpf) {

        isClienteAlreadyExists(cpf);
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);

        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }


    @Transactional
    public ClienteCheckInDTO checkInCliente(String cpf) {
        Optional<Cliente> clienteOptional = clienteRepository.findByCpf(cpf);

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();

            return mapper.map(cliente, ClienteCheckInDTO.class);
        } else {
            throw new ObjectNotFoundException("Cliente não encontrado");
        }
    }

    private void isClienteAlreadyExists(String cpf) {
        Optional<Cliente> clienteAlreadyExists = this.findByCpf(cpf);
        if (clienteAlreadyExists.isPresent()) {
            throw new ClienteAlreadyExistsException("Cliente já cadastrado");
        }
    }

}
