package com.api.costs.usuario.DTOs;

import com.api.costs.orcamento.Orcamento;
import com.api.costs.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


public record DadosCadastroUsuario (

        @NotBlank
        String login,

        List<Orcamento> orcamentos
){

    public DadosCadastroUsuario (Usuario usuario){
        this (usuario.getLogin(), usuario.getOrcamentos());
    }
}
