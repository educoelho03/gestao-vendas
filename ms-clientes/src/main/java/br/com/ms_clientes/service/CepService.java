package br.com.ms_clientes.service;

import br.com.ms_clientes.client.CepClient;
import br.com.ms_clientes.client.response.CepResponse;
import br.com.ms_clientes.exceptions.CepInvalidException;
import org.springframework.stereotype.Service;

@Service
public class CepService {

    private final CepClient cepClient;

    public CepService(CepClient cepClient) {
        this.cepClient = cepClient;
    }

    public CepResponse getAddressByCep(String cep){
        CepResponse response = cepClient.getAddressByCep(cep);

        if(response.getCep() == null){
            throw new CepInvalidException("CEP informado não encontrado");
        }

        return response;
    }

}
