package com.api.costs.usuario.repository;


import com.api.costs.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {


    UserDetails findByLogin(String login);
}
