package com.api.costs.orcamento.DTO;

import com.api.costs.orcamento.Status;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosAtulizarOrcamento(

        @NotNull
        Long id,
        String nome,
        BigDecimal valor,
        @Enumerated
        Status status
        ) {
}
