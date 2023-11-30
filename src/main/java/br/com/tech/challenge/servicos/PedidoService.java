package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.api.exception.StatusPedidoInvalidoException;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.StatusPedidoDTO;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ProdutoService produtoService;

    private final ClienteService clienteService;

    private final PagamentoService pagamentoService;

    private final ModelMapper mapper;

    @Transactional
    public Pedido save(PedidoDTO pedidoDTO) {
        log.info("Salvando pedido {}", pedidoDTO);
        final var produtoList = mapProductListDtoToEntityList(pedidoDTO.getProdutos());
        validateExistingClient(pedidoDTO);
        validateListProductsOrder(pedidoDTO);
        validateProductExisting(produtoList);
        pedidoDTO.setStatusPedido(StatusPedido.RECEBIDO);
        pedidoDTO.setValorTotal(calculateTotalValueProducts(produtoList));
        pedidoDTO.setSenhaRetirada(PasswordUtils.generatePassword());
        pedidoDTO.setDataHora(LocalDateTime.now());
        var pedido = pedidoRepository.save(mapper.map(pedidoDTO, Pedido.class));
        pagamentoService.save(pedido);
        return pedido;
    }

    private void validateExistingClient(PedidoDTO pedidoDTO) {
        log.info("Validando se cliente foi informado e se pode ser encontrado");
        if (Objects.isNull(pedidoDTO.getCliente())) {
            throw new ObjectNotFoundException("Cliente não informado.");
        } else if (!clienteService.existsById(pedidoDTO.getCliente().getId())) {
            throw new ObjectNotFoundException("Cliente não encontrado: " + pedidoDTO.getCliente().getId());
        }
    }

    private void validateListProductsOrder(PedidoDTO pedidoDTO) {
        log.info("Validando se lista de produtos nao e nula nem esta vazia");
        if (Objects.isNull(pedidoDTO.getProdutos()) || pedidoDTO.getProdutos().isEmpty()) {
            throw new ObjectNotFoundException("Lista de produtos vazia");
        }
    }

    private void validateProductExisting(List<Produto> produtos) {
        log.info("Validando se a lista de produtos existe {}", produtos);
        produtos.forEach(produto -> produtoService.findById(produto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado " + produto.getId())));
    }

    private BigDecimal calculateTotalValueProducts(List<Produto> produtos) {
        log.info("Calcular valor total dos produtos {}", produtos);
        return produtos.stream().map(Produto::getValorUnitario).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<Produto> mapProductListDtoToEntityList(final List<ProdutoDTO> produtoDTOList){
        log.info("Mapeando lista de ProdutoDTO {} para lista de Produto", produtoDTOList);
        return mapper.map(
                produtoDTOList,
                new TypeToken<List<Produto>>() {}.getType()
        );
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> list(int pagina, int tamanho) {
        log.info("Listando os pedidos com pagina {} e tamanho {}", pagina, tamanho);
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("id"));

        Page<Pedido> pedidos = pedidoRepository.findAll(pageable);

        List<PedidoDTO> pedidoDTOs = pedidos.getContent().stream()
                .map(pedido -> mapper.map(pedido, PedidoDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(pedidoDTOs, pageable, pedidos.getTotalElements());
    }


    @Transactional
    public PedidoDTO updateStatus(Long pedidoId, StatusPedidoDTO novoStatus) {
        log.info("Atualizando status do pedido {} com novo status {}", pedidoId, novoStatus);
        validarStatusPedido(novoStatus.getStatusPedido());

        if (novoStatus.getStatusPedido().equals(StatusPedido.CANCELADO)) {
            throw new StatusPedidoInvalidoException();
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado: " + pedidoId));

        pedido.setStatusPedido(novoStatus.getStatusPedido());

        return mapper.map(pedidoRepository.save(pedido), PedidoDTO.class);
    }


    private void validarStatusPedido(StatusPedido statusPedido) {
        log.info("Validando se o status do pedido e valido");
        for (StatusPedido enumValue : StatusPedido.values()) {
            if (enumValue == statusPedido) {
                return;
            }
        }
        throw new StatusPedidoInvalidoException();
    }

    private ClienteDTO setClienteAnonimoDTO() {
        return ClienteDTO.builder()
                .id(99L)
                .nome("Usuario Anonimo")
                .email("")
                .cpf("999.999.999-99")
                .build();
    }
    
}
