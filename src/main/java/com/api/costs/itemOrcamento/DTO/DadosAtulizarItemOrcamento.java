package com.api.costs.itemOrcamento.DTO;

import com.api.costs.itemOrcamento.Status;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosAtulizarItemOrcamento(

        @NotNull
        Long id,
        String nome,
        BigDecimal valor,
        @Enumerated
        Status status
        ) {
}
