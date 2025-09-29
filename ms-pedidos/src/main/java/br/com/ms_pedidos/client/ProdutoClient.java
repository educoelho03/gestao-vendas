package br.com.ms_pedidos.client;

import br.com.ms_pedidos.dto.ProdutoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-produtos", url = "${ms_produtos.url:http://localhost:8083}")
public interface ProdutoClient {

    @GetMapping("/produtos/{id}")
    ProdutoDto byId(@PathVariable Integer id);
}
