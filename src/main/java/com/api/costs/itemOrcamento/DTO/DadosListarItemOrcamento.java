package com.api.costs.itemOrcamento.DTO;


import com.api.costs.itemOrcamento.Categoria;
import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.itemOrcamento.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosListarItemOrcamento(
        Long id,
        String nome,
        BigDecimal valor,
        Categoria categoria,
        Status status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao
) {
    public DadosListarItemOrcamento(ItemOrcamento itemOrcamento) {
        this(
                itemOrcamento.getId(),
                itemOrcamento.getNome(),
                itemOrcamento.getValor(),
                itemOrcamento.getCategoria(),
                itemOrcamento.getStatus(),
                itemOrcamento.getDataCriacao(),
                itemOrcamento.getDataAlteracao()
        );
    }
}