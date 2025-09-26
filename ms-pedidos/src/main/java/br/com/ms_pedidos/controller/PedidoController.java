package br.com.ms_pedidos.controller;

import br.com.ms_pedidos.dto.PedidoListDto;
import br.com.ms_pedidos.dto.PedidoSaveDto;
import br.com.ms_pedidos.service.PedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

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

            String messageJson = objectMapper.writeValueAsString(pedidoSaveDto);
            kafkaTemplate.send("product-update-stock", messageJson);

            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }




}
