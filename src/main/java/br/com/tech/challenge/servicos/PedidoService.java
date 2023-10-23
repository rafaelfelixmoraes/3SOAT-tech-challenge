package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.FilaPedidosDTO;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ProdutoRepository produtoRepository;

    private final ClienteRepository clienteRepository;

    private final ModelMapper mapper;

    @Transactional
    public Pedido save(PedidoDTO pedidoDTO) {
        final var produtoList = mapProductListDtoToEnityList(pedidoDTO.getProdutos());
        validateExistingClient(pedidoDTO);
        validListProductsOrder(pedidoDTO);
        validProductExisting(produtoList);
        pedidoDTO.setStatusPedido(StatusPedido.RECEBIDO);
        pedidoDTO.setValorTotal(calculateTotalValueProducts(produtoList));
        pedidoDTO.setSenhaRetirada(PasswordUtils.generatePassword());
        return pedidoRepository.save(mapper.map(pedidoDTO, Pedido.class));
    }

    private void validateExistingClient(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getCliente() == null)
            throw new ObjectNotFoundException("Cliente não informado.");
        else if (!clienteRepository.existsById(pedidoDTO.getCliente().getId()))
            throw new ObjectNotFoundException("Cliente não encontrado " + pedidoDTO.getCliente().getId());
    }

    private void validListProductsOrder(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getProdutos() == null || pedidoDTO.getProdutos().isEmpty())
            throw new ObjectNotFoundException("Lista de produtos vazia");
    }

    private void validProductExisting(List<Produto> produtos) {
        produtos.forEach(produto -> produtoRepository.findById(produto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado " + produto.getId())));
    }

    private BigDecimal calculateTotalValueProducts(List<Produto> produtos) {
        return produtos.stream().map(Produto::getValorUnitario).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<Produto> mapProductListDtoToEnityList(final List<ProdutoDTO> produtoDTOList){
        return mapper.map(
                produtoDTOList,
                new TypeToken<List<Produto>>() {}.getType()
        );
    }

}
