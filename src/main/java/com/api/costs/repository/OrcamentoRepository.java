package com.api.costs.repository;

import com.api.costs.orcamento.Orcamento;
import com.api.costs.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;


public interface OrcamentoRepository extends JpaRepository<Orcamento,Long> {

    List<Orcamento> findAllByDebitadoTrue();

    Page<Orcamento> findByNome(String nome, Pageable page);

    Page<Orcamento> findByUsuario (Usuario usuario, Pageable pageable);

    Page<Orcamento> findByUsuarioAndNomeContaining(Usuario usuario , String nome, Pageable page);

    Optional<Orcamento> findByUsuarioAndId (Usuario usuario, Long id);

}
