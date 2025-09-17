package br.com.ms_pedidos.mapper;

import br.com.ms_pedidos.dto.PedidoListDto;
import br.com.ms_pedidos.entities.Pedido;

import java.util.function.Function;

public class PedidoMapper {
    public final static Function<Pedido, PedidoListDto> entityToListDto = pedido -> {
        PedidoListDto listDto = new PedidoListDto();

        listDto.setId(pedido.getId());
        listDto.setClienteId(pedido.getClienteId());
        listDto.setDataCriacao(pedido.getDataCriacao());
        listDto.setTotal(pedido.getTotal());

        return listDto;
    };
}
