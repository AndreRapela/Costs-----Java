package com.api.costs.repository;


import com.api.costs.parceiro.Parceiro;
import com.api.costs.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParceiroRepository extends JpaRepository<Parceiro,Long> {

    Page<Parceiro> findByUsuario (Usuario usuario, Pageable page);

    Page<Parceiro> fingByNomeContainingIgnoreCase (String nome, Pageable page);

    Page<Parceiro> findByNomeAndUsuarioContaingIgnoreCase(String nome, Pageable page, Usuario usuario);

    Optional<Parceiro> findByUsuarioAndId (Usuario usuario, Long id);
}
