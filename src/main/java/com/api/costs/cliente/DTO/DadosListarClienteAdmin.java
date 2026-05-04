package com.api.costs.cliente.DTO;

import jakarta.validation.constraints.NotBlank;

public record DadosListarClienteAdmin (


        String nome,

        String email,

        String telefone
){}
