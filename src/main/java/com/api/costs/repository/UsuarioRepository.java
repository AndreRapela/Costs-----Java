package com.api.costs.repository;


import com.api.costs.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {


    Usuario findByLogin(String login);

    Page<Usuario> findByLogin( Pageable page, String nome);
}
