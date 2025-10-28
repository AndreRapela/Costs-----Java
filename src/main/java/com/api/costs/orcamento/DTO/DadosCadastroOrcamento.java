package com.api.costs.orcamento.DTO;


import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.usuario.Usuario;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record DadosCadastroOrcamento(

        Long id,

        @NotBlank
        String nome,

        BigDecimal valor,

        @Enumerated
        Categoria categoria,

        @Enumerated
        Status status,

       @NotBlank
       Long usuarioId
){


        public DadosCadastroOrcamento(Orcamento orcamento) {
                this(   orcamento.getId(),
                        orcamento.getNome(),
                        orcamento.getValor(),
                        orcamento.getCategoria(),
                        orcamento.getStatus(),
                        orcamento.getUsuario().getId()
                );
        }
}

