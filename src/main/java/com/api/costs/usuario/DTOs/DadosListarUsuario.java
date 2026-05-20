package com.api.costs.usuario.DTOs;

import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DadosListarUsuario (

        @NotBlank
        String login,

        List<ItemOrcamento> itemOrcamentos

){
    public DadosListarUsuario(Usuario usuario){
        this(usuario.getLogin(),usuario.getItemOrcamentos());
    }

}
