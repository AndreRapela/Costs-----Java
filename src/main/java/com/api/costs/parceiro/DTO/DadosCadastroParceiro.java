package com.api.costs.parceiro.DTO;

import com.api.costs.parceiro.Categoria;
import com.api.costs.parceiro.Parceiro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroParceiro(

       @NotBlank
       String nome,
       @NotBlank
       String email,
       @NotBlank
       String telefone,
       @NotNull
       Categoria categoria

){
    public DadosCadastroParceiro(Parceiro parceiro){
        this(parceiro.getNome(), parceiro.getEmail(), parceiro.getTelefone(),parceiro.getCategoria());
    }
}
