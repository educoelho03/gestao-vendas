package br.com.ms_clientes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfInvalidException extends RuntimeException {

    public CpfInvalidException(String message) {
        super(message);
    }
}
