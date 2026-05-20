package com.api.costs.itemOrcamento.DTO;


import com.api.costs.itemOrcamento.Categoria;
import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.itemOrcamento.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record DadosCadastroItemOrcamentoAdmin(

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


        public DadosCadastroItemOrcamentoAdmin(ItemOrcamento itemOrcamento) {
                this(   itemOrcamento.getNome(),
                        itemOrcamento.getValor(),
                        itemOrcamento.getCategoria(),
                        itemOrcamento.getStatus(),
                        itemOrcamento.getUsuario().getId()
                );
        }
}

