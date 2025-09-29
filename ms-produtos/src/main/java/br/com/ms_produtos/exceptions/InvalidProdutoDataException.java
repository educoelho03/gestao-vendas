package br.com.ms_produtos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProdutoDataException extends RuntimeException {
    public InvalidProdutoDataException(String message) {
        super(message);
    }
}
