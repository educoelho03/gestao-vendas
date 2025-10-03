package br.com.ms_pedidos.controller;

import br.com.ms_pedidos.dto.*;
import br.com.ms_pedidos.service.PedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);
    private PedidoService service;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public PedidoController(PedidoService service, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.service = service;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<PedidoListDto> list(){
        List<PedidoListDto> list = service.list();
        return list;
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody PedidoSaveDto pedidoSaveDto){
        try {
            int id = service.create(pedidoSaveDto);

            if (pedidoSaveDto.getItens() == null || pedidoSaveDto.getItens().isEmpty()) {
                log.warn("Pedido criado (ID: {}) sem itens", id);
                return ResponseEntity.status(HttpStatus.CREATED).body(id);
            }

            for(ItemPedidoSaveDto item : pedidoSaveDto.getItens()){
                EstoqueMessageDto estoqueMessageDto = new EstoqueMessageDto();

                estoqueMessageDto.setProductId(item.getProdutoId());
                estoqueMessageDto.setQuantidade(item.getQuantidade());

                String messageJson = objectMapper.writeValueAsString(estoqueMessageDto);
                kafkaTemplate.send("product-update-stock", messageJson);

                log.info("mensagem enviada para atualizar estoque - Produto {}, Quantidade {}", item.getProdutoId(), item.getQuantidade());
            }

            log.info("Pedido criado (ID: {}) com {} itens processados", id, pedidoSaveDto.getItens().size());

            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }




}
