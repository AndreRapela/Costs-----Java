package com.api.costs.orcamento.DTO;


import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosListarOrcamento(
        Long id,
        String nome,
        BigDecimal valor,
        Categoria categoria,
        Status status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao
) {
    public DadosListarOrcamento(Orcamento orcamento) {
        this(
                orcamento.getId(),
                orcamento.getNome(),
                orcamento.getValor(),
                orcamento.getCategoria(),
                orcamento.getStatus(),
                orcamento.getDataCriacao(),
                orcamento.getDataAlteracao()
        );
    }
}