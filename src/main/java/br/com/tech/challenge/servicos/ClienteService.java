package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.ClienteRepository;
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
        return clienteRepository.save(mapper.map(clienteDTO, Cliente.class));
    }

}
