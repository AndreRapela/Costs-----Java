package com.api.costs.repository;

import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamento,Long> {


    Page<ItemOrcamento> findByNomeContainingIgnoreCase(String nome, Pageable page);

    Page<ItemOrcamento> findByUsuario (Usuario usuario, Pageable pageable);

    Page<ItemOrcamento> findByUsuarioAndNomeContainingIgnoreCase(Usuario usuario , String nome, Pageable page);

    Optional<ItemOrcamento> findByUsuarioAndId (Usuario usuario, Long id);

    @Query(value = "SELECT * FROM tb_orcamento WHERE ativo = false", nativeQuery = true)
    List<ItemOrcamento> listarExcluidos();
}
