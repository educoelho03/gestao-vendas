package br.com.ms_clientes.exceptions.handler;

import br.com.ms_clientes.exceptions.ClienteNotFoundException;
import br.com.ms_clientes.exceptions.errorResponse.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClienteExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleClienteException(ClienteNotFoundException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
