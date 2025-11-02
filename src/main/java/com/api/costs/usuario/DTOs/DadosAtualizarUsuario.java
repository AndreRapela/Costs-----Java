package com.api.costs.usuario.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizarUsuario(


        Long id ,

        @NotBlank
        String senha) {
}
