package com.api.costs.parceiro.DTO;

import com.api.costs.parceiro.Categoria;
import com.api.costs.parceiro.Parceiro;

import java.time.LocalDateTime;

public record DadosListarParceiroAdmin(


        String nome,
        String email,
        String telefone,
        Categoria categoria,
        boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao,
        Long usuarioId
){
    public DadosListarParceiroAdmin(Parceiro parceiro){
        this(
                parceiro.getNome(),
                parceiro.getEmail(),
                parceiro.getTelefone(),
                parceiro.getCategoria(),
                parceiro.isAtivo(),
                parceiro.getDataCriacao(),
                parceiro.getDataAlteracao(),
                parceiro.getUsuario().getId()
                );
    }
}
