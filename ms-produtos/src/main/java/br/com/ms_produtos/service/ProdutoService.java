package br.com.ms_produtos.service;

import br.com.ms_produtos.dto.ProdutoDto;
import br.com.ms_produtos.dto.ProdutoListDto;
import br.com.ms_produtos.dto.ProdutoSaveDto;
import br.com.ms_produtos.entities.Produto;
import br.com.ms_produtos.exceptions.InvalidProdutoDataException;
import br.com.ms_produtos.exceptions.ProdutoNotFoundException;
import br.com.ms_produtos.mapper.ProdutoMapper;
import br.com.ms_produtos.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private static final Logger log = LoggerFactory.getLogger(ProdutoService.class);

    ProdutoRepository repo;
    ObjectMapper objectMapper;

    public ProdutoService(ProdutoRepository repo, ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    public List<ProdutoListDto> list(int page, int itens){
        List<ProdutoListDto> list = repo.findAllPageable(PageRequest.of(page, itens)).stream()
                .map(ProdutoMapper.entityToListDto)
                .collect(Collectors.toList());

        return list;
    }

    @Transactional
    public int create(ProdutoSaveDto produtoSaveDto){
        Produto entity = new Produto();

        if (produtoSaveDto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProdutoDataException("Preço não pode ser negativo.");
        }

        entity.setId(produtoSaveDto.getId());
        entity.setNome(produtoSaveDto.getNome());
        entity.setDescricao(produtoSaveDto.getDescricao());
        entity.setPreco(produtoSaveDto.getPreco());
        entity.setQuantidadeEstoque(produtoSaveDto.getQuantidadeEstoque());

        repo.save(entity);

        return entity.getId();
    }

    @Transactional
    public boolean update(ProdutoDto produtoDto, int id){
        Produto entity = repo.findById(id);

        if(entity == null){
            throw new ProdutoNotFoundException("Não foi encontrado um produto com esse id: " + id);
        }

        entity.setNome(produtoDto.getNome());
        entity.setDescricao(produtoDto.getDescricao());
        entity.setPreco(produtoDto.getPreco());
        entity.setQuantidadeEstoque(produtoDto.getQuantidadeEstoque());

        repo.save(entity);

        return true;
    }

    public ProdutoDto byId(int id){
        Produto entity = repo.findById(id);

        if(entity == null){
            throw new ProdutoNotFoundException("Não foi encontrado um produto com esse id: " + id);
        }

        ProdutoDto dto = ProdutoMapper.entityToDto.apply(entity);
        return dto;
    }
}
