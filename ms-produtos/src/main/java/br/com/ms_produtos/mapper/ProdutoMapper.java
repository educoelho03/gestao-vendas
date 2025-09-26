package br.com.ms_produtos.mapper;

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
}
