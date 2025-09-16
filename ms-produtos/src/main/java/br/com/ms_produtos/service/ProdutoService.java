package br.com.ms_produtos.service;

import br.com.ms_produtos.dto.ProdutoDto;
import br.com.ms_produtos.dto.ProdutoListDto;
import br.com.ms_produtos.dto.ProdutoSaveDto;
import br.com.ms_produtos.entities.Produto;
import br.com.ms_produtos.mapper.ProdutoMapper;
import br.com.ms_produtos.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService{

    ProdutoRepository repo;

    public ProdutoService(ProdutoRepository repo) {
        this.repo = repo;
    }

    public List<ProdutoListDto> list(){
        List<ProdutoListDto> list = repo.findAll().stream()
                .map(ProdutoMapper.entityToListDto)
                .collect(Collectors.toList());

        return list;
    }

    @Transactional
    public int create(ProdutoSaveDto produtoSaveDto){
        Produto entity = new Produto();

        entity.setId(produtoSaveDto.getId());
        entity.setNome(produtoSaveDto.getNome());
        entity.setDescricao(produtoSaveDto.getDescricao());
        entity.setPreco(produtoSaveDto.getPreco());

        repo.save(entity);

        return entity.getId();
    }

    @Transactional
    public boolean update(ProdutoDto produtoDto, int id){
        Produto entity = repo.findById(id);

        if(entity == null){
            return false;
        }

        entity.setNome(produtoDto.getNome());
        entity.setDescricao(produtoDto.getDescricao());
        entity.setPreco(produtoDto.getPreco());

        repo.save(entity);

        return true;
    }

}
