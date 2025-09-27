package br.com.ms_pedidos.dto;

public class EstoqueMessageDto {
    private Integer productId;
    private Integer quantidade;

    // Construtores, getters e setters
    public EstoqueMessageDto() {}

    public EstoqueMessageDto(Integer productId, Integer quantidade) {
        this.productId = productId;
        this.quantidade = quantidade;
    }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
