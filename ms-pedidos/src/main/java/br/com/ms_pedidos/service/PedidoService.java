package br.com.ms_pedidos.service;

import br.com.ms_pedidos.client.ProdutoClient;
import br.com.ms_pedidos.dto.ItemPedidoSaveDto;
import br.com.ms_pedidos.dto.PedidoListDto;
import br.com.ms_pedidos.dto.PedidoSaveDto;
import br.com.ms_pedidos.dto.ProdutoDto;
import br.com.ms_pedidos.entities.ItemPedido;
import br.com.ms_pedidos.entities.Pedido;
import br.com.ms_pedidos.mapper.PedidoMapper;
import br.com.ms_pedidos.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProdutoClient produtoClient;

    public PedidoService(PedidoRepository pedidoRepo, ProdutoClient produtoClient) {
        this.pedidoRepo = pedidoRepo;
        this.produtoClient = produtoClient;
    }

    @Transactional
    public Integer create(PedidoSaveDto pedidoDto) {
        Pedido entity = new Pedido();
        entity.setClienteId(pedidoDto.getClienteId());
        entity.setDataCriacao(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        if (pedidoDto.getItens() != null) {
            for (ItemPedidoSaveDto itemDto : pedidoDto.getItens()) {
                ProdutoDto produto = produtoClient.byId(itemDto.getProdutoId());

                BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
                total = total.add(subtotal);

                ItemPedido item = new ItemPedido();
                item.setProdutoId(itemDto.getProdutoId());
                item.setQuantidade(itemDto.getQuantidade());
                item.setPedido(entity);

                entity.getItens().add(item);
            }
        }

        entity.setTotal(total);

        pedidoRepo.save(entity);
        return entity.getId();
    }

    public List<PedidoListDto> list(){
        List<PedidoListDto> list = pedidoRepo.findAllWithItens().stream()
                .map(PedidoMapper.entityToListDto)
                .collect(Collectors.toList());

        return list;
    }
}
