package com.api.costs.parceiro.DTO;

import com.api.costs.parceiro.Categoria;
import com.api.costs.parceiro.Parceiro;

import java.time.LocalDateTime;

public record DadosListarParceiro(

        Long id,
        String nome,
        String email,
        String telefone,
        Categoria categoria,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao
){
    public DadosListarParceiro(Parceiro parceiro){
        this(
                parceiro.getId(),
                parceiro.getNome(),
                parceiro.getEmail(),
                parceiro.getTelefone(),
                parceiro.getCategoria(),
                parceiro.getDataCriacao(),
                parceiro.getDataAlteracao()
        );
    }
}
