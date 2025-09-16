package br.com.ms_clientes.client;

import br.com.ms_clientes.client.response.CepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ViaCepClient", url = "https://viacep.com.br/ws")
public interface CepClient {

    @GetMapping("/{cep}/json")
    CepResponse getAddressByCep(@PathVariable("cep") String cep);

}
