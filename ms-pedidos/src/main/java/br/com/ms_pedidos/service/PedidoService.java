package br.com.ms_pedidos.service;

import br.com.ms_pedidos.dto.ItemPedidoSaveDto;
import br.com.ms_pedidos.dto.PedidoListDto;
import br.com.ms_pedidos.dto.PedidoSaveDto;
import br.com.ms_pedidos.entities.ItemPedido;
import br.com.ms_pedidos.entities.Pedido;
import br.com.ms_pedidos.mapper.PedidoMapper;
import br.com.ms_pedidos.repository.ItemPedidoRepository;
import br.com.ms_pedidos.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    PedidoRepository pedidoRepo;
    ItemPedidoRepository itemPedidoRepo;

    public PedidoService(PedidoRepository pedidoRepo, ItemPedidoRepository itemPedidoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.itemPedidoRepo = itemPedidoRepo;
    }

    @Transactional
    public Integer create(PedidoSaveDto pedidoDto){
        Pedido entity = new Pedido();

        entity.setClienteId(pedidoDto.getClienteId());
        entity.setDataCriacao(LocalDateTime.now());
        entity.setTotal(BigDecimal.ZERO);

        pedidoRepo.save(entity);

        BigDecimal total = BigDecimal.ZERO;

        // processa e salva os itens do pedido
        if (pedidoDto.getItens() != null) {
            for (ItemPedidoSaveDto itemDto : pedidoDto.getItens()) {
                ItemPedido item = new ItemPedido();
                item.setPedido(entity);
                item.setPedido(entity);
                item.setProdutoId(itemDto.getProdutoId());
                item.setQuantidade(itemDto.getQuantidade());


                entity.getItens().add(item);
            }

            entity.setTotal(total);
        }

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
