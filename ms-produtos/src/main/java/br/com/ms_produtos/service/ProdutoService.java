package br.com.ms_produtos.service;

import br.com.ms_produtos.dto.EstoqueMessageDto;
import br.com.ms_produtos.dto.ProdutoDto;
import br.com.ms_produtos.dto.ProdutoListDto;
import br.com.ms_produtos.dto.ProdutoSaveDto;
import br.com.ms_produtos.entities.Produto;
import br.com.ms_produtos.mapper.ProdutoMapper;
import br.com.ms_produtos.repository.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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
        entity.setQuantidadeEstoque(produtoSaveDto.getQuantidadeEstoque());

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
        entity.setQuantidadeEstoque(produtoDto.getQuantidadeEstoque());

        repo.save(entity);

        return true;
    }

    @Transactional
    @KafkaListener(topics = "product-update-stock", groupId = "produto-group")
    public void updateProductStock(String mensagemJson){
        try {
            EstoqueMessageDto estoqueMessage = objectMapper.readValue(mensagemJson, EstoqueMessageDto.class);

            Produto entity = repo.findById(estoqueMessage.getProductId());

            if(entity == null){
                log.error("Produto não pode ser nulo");
                return; // usar return ao inves de exception pois com exception pode cair em um loop infinito, travando a fila de mensagens
            }

            if(entity.getQuantidadeEstoque() < estoqueMessage.getQuantidade()){
                log.error("Quantidade em estoque insuficiente para realizar o pedido. Produto: {}; Estoque atual: {}; Quantidade solicitada: {}", entity.getId(), entity.getQuantidadeEstoque(), estoqueMessage.getQuantidade());
                return;
            }

            int novoEstoque = entity.getQuantidadeEstoque() - estoqueMessage.getQuantidade();
            entity.setQuantidadeEstoque(novoEstoque);

            repo.save(entity);

            log.info("Estoque do produto: {} atualizado com sucesso. Novo estoque: {}", entity.getId(), novoEstoque);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
