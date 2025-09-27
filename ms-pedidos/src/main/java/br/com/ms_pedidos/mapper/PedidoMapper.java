package br.com.ms_pedidos.mapper;

import br.com.ms_pedidos.dto.ItemPedidoListDto;
import br.com.ms_pedidos.dto.PedidoListDto;
import br.com.ms_pedidos.entities.ItemPedido;
import br.com.ms_pedidos.entities.Pedido;

import java.util.function.Function;
import java.util.stream.Collectors;

public class PedidoMapper {
    public final static Function<Pedido, PedidoListDto> entityToListDto = pedido -> {
        PedidoListDto listDto = new PedidoListDto();

        listDto.setId(pedido.getId());
        listDto.setClienteId(pedido.getClienteId());
        listDto.setDataCriacao(pedido.getDataCriacao());

        if (pedido.getItens() != null){
            listDto.setItens(pedido.getItens().stream()
                    .map(PedidoMapper::toItemDto)
                    .collect(Collectors.toList()));
        }

        listDto.setTotal(pedido.getTotal());

        return listDto;
    };


    private static ItemPedidoListDto toItemDto(ItemPedido item) {
        ItemPedidoListDto dto = new ItemPedidoListDto();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProdutoId());
        dto.setQuantidade(item.getQuantidade());
        return dto;
    }
}
