package br.com.ms_clientes.controller;

import br.com.ms_clientes.dto.ClienteDto;
import br.com.ms_clientes.dto.ClienteListDto;
import br.com.ms_clientes.dto.ClienteSaveDto;
import br.com.ms_clientes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    ClienteService service;

    @Autowired
    private KafkaTemplate<String, ClienteSaveDto> kafkaTemplate;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteListDto> list(){
        List<ClienteListDto> list = service.list();
        return list;
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody ClienteSaveDto clienteSaveDto){
        int id = service.create(clienteSaveDto);
        kafkaTemplate.send("cliente-added-topic", clienteSaveDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
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
