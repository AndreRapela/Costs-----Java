package com.api.costs.infra.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private Instant timestamp = Instant.now();
    private Integer status;
    private String error;
    private String message;
    private String path;
    private String stackTrace;
    private List<FieldValidationError> filedErrors;


    public record FieldValidationError (String campo, String mensagem){

        public FieldValidationError (FieldError erro ){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
