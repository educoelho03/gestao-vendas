package br.com.ms_clientes.service;

import br.com.ms_clientes.client.response.CepResponse;
import br.com.ms_clientes.dto.ClienteDto;
import br.com.ms_clientes.dto.ClienteListDto;
import br.com.ms_clientes.dto.ClienteSaveDto;
import br.com.ms_clientes.entity.Cliente;
import br.com.ms_clientes.exceptions.CepInvalidException;
import br.com.ms_clientes.exceptions.CpfInvalidException;
import br.com.ms_clientes.exceptions.EmailInvalidException;
import br.com.ms_clientes.mapper.ClienteMapper;
import br.com.ms_clientes.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    ClienteRepository repository;
    CepService cepService;
    PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository repository, CepService cepService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.cepService = cepService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ClienteListDto> list(int page, int itens){
        Pageable pageable = PageRequest.of(page, itens);

        List<ClienteListDto> list = repository.findAll(pageable).stream()
                .map(ClienteMapper.entityToListDto)
                .collect(Collectors.toList());

        return list;
    }

    @Transactional
    public int create(ClienteSaveDto clienteSaveDto){
        Cliente entity = new Cliente();

        if (clienteSaveDto.getCep() == null || clienteSaveDto.getCep().isEmpty()) {
            throw new CepInvalidException("CEP NÃO pode ser nulo ou vazio");
        }

        if (clienteSaveDto.getCpf() == null || clienteSaveDto.getCpf().isEmpty()) {
            throw new CpfInvalidException("CPF não pode ser nulo ou vazio");
        }

        if(clienteSaveDto.getEmail() == null || clienteSaveDto.getEmail().isEmpty()){
            throw new EmailInvalidException("Email NÃO pode ser nulo ou vazio");
        }

        CepResponse cepResponse = cepService.getAddressByCep(clienteSaveDto.getCep());

        entity.setId(clienteSaveDto.getId());
        entity.setNome(clienteSaveDto.getNome());
        entity.setSobrenome(clienteSaveDto.getSobrenome());
        entity.setCpf(clienteSaveDto.getCpf());
        entity.setCep(cepResponse.getCep());
        entity.setEmail(clienteSaveDto.getEmail());
        entity.setPassword(passwordEncoder.encode(clienteSaveDto.getPassword()));

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
            throw new CepInvalidException("CEP NÃO pode ser nulo ou vazio");
        }

        if(clienteDto.getCpf() == null || clienteDto.getCpf().isEmpty()){
            throw new CpfInvalidException("CPF NÃO pode ser nulo ou vazio");
        }

        if(clienteDto.getEmail() == null || clienteDto.getEmail().isEmpty()){
            throw new EmailInvalidException("Email NÃO pode ser nulo ou vazio");
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
