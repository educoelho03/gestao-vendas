package br.com.ms_clientes.controller;

import br.com.ms_clientes.client.response.CepResponse;
import br.com.ms_clientes.service.CepService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CepController {

    private CepService cepService;

    @PostMapping("/{cep}/json")
    public CepResponse buscarPeloCep(String cep){
        return cepService.getAddressByCep(cep);
    }
}
