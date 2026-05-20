package com.api.costs.itemOrcamento.DTO;

import com.api.costs.itemOrcamento.Categoria;
import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.itemOrcamento.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosListarItemOrcamentoAdmin(

        Long id,
        String nome,
        BigDecimal valor,
        Categoria categoria,
        Status status,
        boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao,
        Long usuarioId
) {
    public DadosListarItemOrcamentoAdmin(ItemOrcamento itemOrcamento) {
        this(
                itemOrcamento.getId(),
                itemOrcamento.getNome(),
                itemOrcamento.getValor(),
                itemOrcamento.getCategoria(),
                itemOrcamento.getStatus(),
                itemOrcamento.isAtivo(),
                itemOrcamento.getDataCriacao(),
                itemOrcamento.getDataAlteracao(),
                itemOrcamento.getUsuario().getId()
        );
    }
}
