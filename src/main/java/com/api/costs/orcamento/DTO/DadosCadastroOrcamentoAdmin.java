package com.api.costs.orcamento.DTO;


import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record DadosCadastroOrcamentoAdmin(

        @NotBlank
        String nome,

        @NotNull
        BigDecimal valor,

        @NotNull
        Categoria categoria,

        @NotNull
        Status status,

        @NotNull
        Long usuarioId
){


        public DadosCadastroOrcamentoAdmin(Orcamento orcamento) {
                this(   orcamento.getNome(),
                        orcamento.getValor(),
                        orcamento.getCategoria(),
                        orcamento.getStatus(),
                        orcamento.getUsuario().getId()
                );
        }
}

