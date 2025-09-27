package br.com.ms_pedidos.dto;

import br.com.ms_pedidos.entities.ItemPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoSaveDto {
    private Integer id;
    private Integer clienteId;
    private LocalDateTime dataCriacao;
    private List<ItemPedidoSaveDto> itens;
    private BigDecimal total;

    public Integer getId() {
        return id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public List<ItemPedidoSaveDto> getItens() {
        return itens;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
