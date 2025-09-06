package com.api.costs.infra.exception;

import lombok.*;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private List<FieldValidationError> filedErrors;


    public static record FieldValidationError (String campo, String mensagem){

        public FieldValidationError (FieldError erro ){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
