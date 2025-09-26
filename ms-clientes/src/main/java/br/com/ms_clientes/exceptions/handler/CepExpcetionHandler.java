package br.com.ms_clientes.exceptions.handler;

import br.com.ms_clientes.exceptions.CepNotFoundException;
import br.com.ms_clientes.exceptions.errorResponse.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CepExpcetionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleCepException(CepNotFoundException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
