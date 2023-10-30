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

    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    private void validateIfClientExistsByCpf(String cpf) {
        Optional<Cliente> clienteAlreadyExists = this.findByCpf(cpf);
        if (clienteAlreadyExists.isPresent()) {
            throw new ClienteAlreadyExistsException("Cliente já cadastrado");
        }
    }

    @Transactional
    public ClienteCheckInDTO checkInCliente(String cpf) {
        Optional<Cliente> clienteOptional = Optional.ofNullable(clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado")));

            return mapper.map(clienteOptional.get(), ClienteCheckInDTO.class);
    }

}
