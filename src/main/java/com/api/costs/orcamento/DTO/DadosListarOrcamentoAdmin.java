package com.api.costs.orcamento.DTO;

import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosListarOrcamentoAdmin(

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
    public DadosListarOrcamentoAdmin(Orcamento orcamento) {
        this(
                orcamento.getId(),
                orcamento.getNome(),
                orcamento.getValor(),
                orcamento.getCategoria(),
                orcamento.getStatus(),
                orcamento.isAtivo(),
                orcamento.getDataCriacao(),
                orcamento.getDataAlteracao(),
                orcamento.getUsuario().getId()
        );
    }
}
