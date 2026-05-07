package com.api.costs.parceiro.DTO;

import com.api.costs.parceiro.Categoria;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizarParceiro(

        @NotNull
        Long id,
        String nome,
        String email,
        String telefone,
        Categoria categoria

){}
