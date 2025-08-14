package com.api.costs.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> error404 (EntityNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError();
                err.setTimestamp(Instant.now());
                err.setStatus(HttpStatus.NOT_FOUND.value());
                err.setError("Resource not found");
                err.setMessage(e.getMessage());
                err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> error400 (MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getFieldErrors().stream().map(Dadoerros::new).toList());
    }

    public record Dadoerros (String campo, String Mensagem){

        public Dadoerros (FieldError erro ){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
