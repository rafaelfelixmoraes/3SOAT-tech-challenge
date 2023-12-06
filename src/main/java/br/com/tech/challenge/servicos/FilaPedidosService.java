package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.FilaPedidosRepository;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilaPedidosService {

    private final FilaPedidosRepository filaPedidosRepository;

    public Page<FilaPedidos> listFilaPedidos(final Integer pagina, final Integer tamanho) {
        log.info("Listando fila de pedidos com pagina {} e tamanho {}", pagina, tamanho);
        final var pageable = PageRequest.of(pagina, tamanho, Sort.by("dataHora").descending());

        return filaPedidosRepository.findAll(pageable);
    }

}
