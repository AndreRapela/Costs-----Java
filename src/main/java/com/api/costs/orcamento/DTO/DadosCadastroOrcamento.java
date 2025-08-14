package com.api.costs.orcamento.DTO;


import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;


public record DadosCadastroOrcamento(

        @NotBlank
        String nome,

        Double valor,

        @Enumerated
        Categoria categoria,

        @Enumerated
        Status status
){


        public DadosCadastroOrcamento(Orcamento orcamento) {
                this(   orcamento.getNome(),
                        orcamento.getValor(),
                        orcamento.getCategoria(),
                        orcamento.getStatus());
        }
}

