package com.api.costs.cliente.DTO;

import com.api.costs.cliente.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroClienteAdmin (

        @NotBlank
        String nome,

        @NotBlank
        String email,

        @NotBlank
        String telefone,

        @NotNull
        Long usuarioId

){
    public DadosCadastroClienteAdmin( Cliente cliente){
        this(cliente.getNome(),cliente.getEmail(), cliente.getTelefone(),cliente.getUsuario().getId());
    }
}
