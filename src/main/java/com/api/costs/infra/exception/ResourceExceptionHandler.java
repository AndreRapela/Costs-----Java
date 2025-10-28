package com.api.costs.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Constants;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
@Slf4j
@RestControllerAdvice
public class ResourceExceptionHandler {

    private boolean printStacktrace = true;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> error404 (EntityNotFoundException e, HttpServletRequest request){
        ApiError err = new ApiError();
                err.setStatus(HttpStatus.NOT_FOUND.value());
                err.setError("Resource not found");
                err.setMessage(e.getMessage());
                err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> error422 (MethodArgumentNotValidException e, HttpServletRequest request) {
        ApiError err = new ApiError();
        err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        err.setError("Validation failed");
        err.setMessage("Campo(s) invalido(s)");
        err.setPath(request.getRequestURI());
        err.setFiledErrors(e.getFieldErrors().stream()
                .map(ApiError.FieldValidationError::new).toList()
        );

        return ResponseEntity.status(err.getStatus()).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> erro500(Exception e , HttpServletRequest request){
        log.error("Erro inesperado", e);

        ApiError err = new ApiError();
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setError("Unexpected error");
        err.setMessage("Ocorreu um erro inesperado!");
        err.setPath(request.getRequestURI());

        if(this.printStacktrace){
            err.setStackTrace(ExceptionUtils.getStackTrace(e));
        }

        return ResponseEntity.status(err.getStatus()).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> erro409 (DataIntegrityViolationException e, HttpServletRequest request){
        log.error("Erro de integridade de dados", e);

        ApiError err = new ApiError();
        err.setStatus(HttpStatus.CONFLICT.value());
        err.setError("Data Integrity violation");
        err.setMessage("Operação não pode ser concluída devido a violação de integridade");
        err.setPath(request.getRequestURI());

        if(this.printStacktrace && e.getCause() != null){
            err.setMessage(err.getMessage() + " Causa " + e.getRootCause().getMessage());
        }

        return ResponseEntity.status(err.getStatus()).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> erro400 (ConstraintViolationException e, HttpServletRequest request){
        ApiError err = new ApiError();
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Invalid request params");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(err.getStatus()).body(err);
    }




}
