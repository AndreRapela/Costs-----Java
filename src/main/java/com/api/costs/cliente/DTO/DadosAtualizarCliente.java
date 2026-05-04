package com.api.costs.cliente.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizarCliente (

        @NotNull
        Long id,
        String nome,
        String email,
        String telefone

){}
