package br.com.ms_clientes.mapper;

import br.com.ms_clientes.dto.ClienteListDto;
import br.com.ms_clientes.entity.Cliente;

import java.util.function.Function;

public class ClienteMapper {
    public final static Function<Cliente, ClienteListDto> entityToListDto = cliente -> {
        ClienteListDto dto = new ClienteListDto();

        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setSobrenome(cliente.getSobrenome());
        dto.setCpf(cliente.getCpf());
        dto.setCep(cliente.getCep());

        return dto;
    };
}
