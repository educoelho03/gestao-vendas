package br.com.ms_clientes.controller;

import br.com.ms_clientes.dto.ClienteDto;
import br.com.ms_clientes.dto.ClienteListDto;
import br.com.ms_clientes.dto.ClienteSaveDto;
import br.com.ms_clientes.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private ClienteService service;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public ClienteController(ClienteService service, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.service = service;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
;
    @GetMapping
    public List<ClienteListDto> list(){
        List<ClienteListDto> list = service.list();
        return list;
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@Valid @RequestBody ClienteSaveDto clienteSaveDto){
        try {
            int id = service.create(clienteSaveDto);

            String mensagemJson = objectMapper.writeValueAsString(clienteSaveDto);
            kafkaTemplate.send("cliente-added-topic", mensagemJson);

            return ResponseEntity.status(HttpStatus.CREATED).body(id);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar objeto para JSON", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(ClienteDto clienteDto, @PathVariable("id") int id){
        boolean flag = service.update(clienteDto, id);

        if(flag){
            return ResponseEntity.ok(flag);
        } else {
            throw new RuntimeException("ERROR NO CONTROLLER DO CLIENTE");
        }
    }

}
