package com.api.costs.orcamento.repository;

import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento,Long> {

    List<Orcamento> findAllByDebitadoTrue();
}
