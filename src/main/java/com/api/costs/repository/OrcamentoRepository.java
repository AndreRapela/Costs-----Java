package com.api.costs.repository;

import com.api.costs.orcamento.Orcamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrcamentoRepository extends JpaRepository<Orcamento,Long> {

    List<Orcamento> findAllByDebitadoTrue();

    Page<Orcamento> findByNome(String nome, Pageable page);

}
