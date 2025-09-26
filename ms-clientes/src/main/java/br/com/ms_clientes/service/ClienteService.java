package br.com.ms_clientes.service;

import br.com.ms_clientes.client.response.CepResponse;
import br.com.ms_clientes.dto.ClienteDto;
import br.com.ms_clientes.dto.ClienteListDto;
import br.com.ms_clientes.dto.ClienteSaveDto;
import br.com.ms_clientes.entity.Cliente;
import br.com.ms_clientes.mapper.ClienteMapper;
import br.com.ms_clientes.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    ClienteRepository repository;
    CepService cepService;

    public ClienteService(ClienteRepository repository, CepService cepService) {
        this.repository = repository;
        this.cepService = cepService;
    }

    public List<ClienteListDto> list(){
        List<ClienteListDto> list = repository.findAll().stream()
                .map(ClienteMapper.entityToListDto)
                .collect(Collectors.toList());

        return list;
    }

    @Transactional
    public int create(ClienteSaveDto clienteSaveDto){
        Cliente entity = new Cliente();

        CepResponse cepResponse = cepService.getAddressByCep(clienteSaveDto.getCep());

        if(clienteSaveDto.getCep() == null || clienteSaveDto.getCep().isEmpty()){
            throw new RuntimeException("CEP nulo ou vazio não são permitidos");
        }

        entity.setId(clienteSaveDto.getId());
        entity.setNome(clienteSaveDto.getNome());
        entity.setSobrenome(clienteSaveDto.getSobrenome());
        entity.setCpf(clienteSaveDto.getCpf());
        entity.setCep(cepResponse.getCep());
        entity.setEmail(clienteSaveDto.getEmail());

        repository.save(entity);

        return entity.getId();
    }

    @Transactional
    public boolean update(ClienteDto clienteDto, int id){
        Cliente entity = repository.findById(id);

        if(entity == null){
            return false;
        }

        if(clienteDto.getCep() == null || clienteDto.getCep().isEmpty()){
            throw new RuntimeException("CEP nulo ou vazio não são permitidos");
        }

        entity.setNome(clienteDto.getNome());
        entity.setSobrenome(clienteDto.getSobrenome());
        entity.setCpf(clienteDto.getCpf());
        entity.setCep(clienteDto.getCep());
        entity.setEmail(clienteDto.getEmail());

        repository.save(entity);

        return true;
    }

}
