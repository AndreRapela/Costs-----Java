package com.api.costs.usuario.DTOs;

import com.api.costs.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;


public record DadosCadastroUsuario (

        @NotBlank
        String login,

        @NotBlank
        String senha
){

    public DadosCadastroUsuario (Usuario usuario){
        this (usuario.getLogin(), usuario.getSenha());
    }
}
