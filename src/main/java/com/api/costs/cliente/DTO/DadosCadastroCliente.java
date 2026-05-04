package com.api.costs.cliente.DTO;

import com.api.costs.cliente.Cliente;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCliente (

       @NotBlank
       String nome,
       @NotBlank
       String email,
       @NotBlank
       String telefone

){
    public DadosCadastroCliente(Cliente cliente){
        this(cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}
