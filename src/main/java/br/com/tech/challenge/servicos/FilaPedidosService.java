package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.FilaPedidosRepository;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilaPedidosService {

    private final FilaPedidosRepository filaPedidosRepository;

    public List<FilaPedidos> listaFilaPedidos(){
        return filaPedidosRepository.findAll();
    }
}
