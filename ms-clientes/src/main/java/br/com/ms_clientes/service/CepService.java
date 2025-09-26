package br.com.ms_clientes.service;

import br.com.ms_clientes.client.CepClient;
import br.com.ms_clientes.client.response.CepResponse;
import br.com.ms_clientes.exceptions.CepNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CepService {

    private final CepClient cepClient;

    public CepService(CepClient cepClient) {
        this.cepClient = cepClient;
    }

    public CepResponse getAddressByCep(String cep){
        CepResponse response = cepClient.getAddressByCep(cep);

        if(response.getCep() == null){
            throw new CepNotFoundException("CEP informado não encontrado");
        }

        return response;
    }

}
