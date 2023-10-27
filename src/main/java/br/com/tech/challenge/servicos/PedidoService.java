package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ProdutoService produtoService;

    private final ClienteService clienteService;

    private final PagamentoService pagamentoService;

    private final ModelMapper mapper;

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido save(PedidoDTO pedidoDTO) {
        final var produtoList = mapProductListDtoToEntityList(pedidoDTO.getProdutos());
        validateExistingClient(pedidoDTO);
        validateListProductsOrder(pedidoDTO);
        validateProductExisting(produtoList);
        pedidoDTO.setStatusPedido(StatusPedido.RECEBIDO);
        pedidoDTO.setValorTotal(calculateTotalValueProducts(produtoList));
        pedidoDTO.setSenhaRetirada(PasswordUtils.generatePassword());
        pedidoDTO.setDataHora(LocalDateTime.now());
        var pedido = pedidoRepository.save(mapper.map(pedidoDTO, Pedido.class));
        var totalValue = calculateTotalValueProducts(produtoList);
        pagamentoService.save(pedido, totalValue);
        return pedido;
    }

    private void validateExistingClient(PedidoDTO pedidoDTO) {
        if (Objects.isNull(pedidoDTO.getCliente())) {
            throw new ObjectNotFoundException("Cliente não informado.");
        } else if (!clienteService.existsById(pedidoDTO.getCliente().getId())) {
            throw new ObjectNotFoundException("Cliente não encontrado: " + pedidoDTO.getCliente().getId());
        }
    }

    private void validateListProductsOrder(PedidoDTO pedidoDTO) {
        if (Objects.isNull(pedidoDTO.getProdutos()) || pedidoDTO.getProdutos().isEmpty()) {
            throw new ObjectNotFoundException("Lista de produtos vazia");
        }
    }

    private void validateProductExisting(List<Produto> produtos) {
        produtos.forEach(produto -> produtoService.findById(produto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado " + produto.getId())));
    }

    private BigDecimal calculateTotalValueProducts(List<Produto> produtos) {
        return produtos.stream().map(Produto::getValorUnitario).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<Produto> mapProductListDtoToEntityList(final List<ProdutoDTO> produtoDTOList){
        return mapper.map(
                produtoDTOList,
                new TypeToken<List<Produto>>() {}.getType()
        );
    }

}
