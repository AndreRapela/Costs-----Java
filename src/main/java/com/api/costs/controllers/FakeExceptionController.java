package com.api.costs.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fake")
public class FakeExceptionController {

    @GetMapping("/not-found")
    public void notFound(){
        throw new EntityNotFoundException("Recurso não encontrado no banco");
    }


    @GetMapping("/validation")
    public void validation(@RequestBody @Valid FakeDTO fakeDTO) {

    }


    @GetMapping("/constraint")
    public void constrainsError(){
        throw new ConstraintViolationException("Parâmetros invalidos na requisição", null);
    }


    @GetMapping("/integrity")
    public void integrity(){
        throw new DataIntegrityViolationException("Violação de integridade", new RuntimeException("Chave duplicada"));
    }


    @GetMapping("/unexpected")
    public void unexpected(){
        throw new RuntimeException("Erro inesperado no servidor");
    }


    @Data
    public static class FakeDTO{

        @NotBlank
        private String nome;

        @NotNull
        private Integer valor;
    }
}
