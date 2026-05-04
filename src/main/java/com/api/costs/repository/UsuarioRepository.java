package com.api.costs.repository;


import com.api.costs.orcamento.Orcamento;
import com.api.costs.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Usuario findByLogin( String login);

    Page<Usuario> findByLoginContainingIgnoreCase(  String nome,Pageable page);

    @Query(value = "SELECT * FROM tb_orcamento WHERE ativo = false", nativeQuery = true)
    List<Usuario> listarExcluidos();
}
