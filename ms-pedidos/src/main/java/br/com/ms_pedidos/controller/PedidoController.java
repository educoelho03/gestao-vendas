package br.com.ms_pedidos.controller;

import br.com.ms_pedidos.dto.PedidoListDto;
import br.com.ms_pedidos.dto.PedidoSaveDto;
import br.com.ms_pedidos.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<PedidoListDto> list(){
        List<PedidoListDto> list = service.list();
        return list;
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody PedidoSaveDto pedidoSaveDto){
        int id = service.create(pedidoSaveDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }




}
