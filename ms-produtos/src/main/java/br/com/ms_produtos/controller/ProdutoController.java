package br.com.ms_produtos.controller;

import br.com.ms_produtos.dto.ProdutoDto;
import br.com.ms_produtos.dto.ProdutoListDto;
import br.com.ms_produtos.dto.ProdutoSaveDto;
import br.com.ms_produtos.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProdutoListDto> list(){
        List<ProdutoListDto> list = service.list();
        return list;
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody ProdutoSaveDto produtoSaveDto){
        int id = service.create(produtoSaveDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(ProdutoDto produtoDto, @PathVariable("id") int id){
        boolean flag = service.update(produtoDto, id);

        if(flag){
            return ResponseEntity.ok(flag);
        } else {
            throw new RuntimeException("ERROR NO CONTROLLER DO PRODUTO");
        }
    }
}
