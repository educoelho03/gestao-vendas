package br.com.ms_produtos.mapper;

import br.com.ms_produtos.dto.ProdutoDto;
import br.com.ms_produtos.dto.ProdutoListDto;
import br.com.ms_produtos.entities.Produto;

import java.util.function.Function;

public class ProdutoMapper {
    public final static Function<Produto, ProdutoListDto> entityToListDto = produto -> {
        ProdutoListDto listDto = new ProdutoListDto();

        listDto.setId(produto.getId());
        listDto.setNome(produto.getNome());
        listDto.setDescricao(produto.getDescricao());
        listDto.setPreco(produto.getPreco());
        listDto.setQuantidadeEstoque(produto.getQuantidadeEstoque());

        return listDto;
    };

    public final static Function<Produto, ProdutoDto> entityToDto = produto -> {
        ProdutoDto dto = new ProdutoDto();

        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());

        return dto;
    };
}
