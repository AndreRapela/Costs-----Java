package com.api.costs.orcamento.DTO;

import com.api.costs.orcamento.Status;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record DadosAtulizarOrcamento(

        @NotNull
        long id,
        String nome,
        Double valor,
        @Enumerated
        Status status
        ) {
}
