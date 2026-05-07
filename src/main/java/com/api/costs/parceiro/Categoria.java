package com.api.costs.parceiro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Categoria {

    CLIENTE,
    FORNECEDOR;

    @JsonCreator
    public static com.api.costs.parceiro.Categoria fromString(String value){
        return value == null ? null : com.api.costs.parceiro.Categoria.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue(){
        return name();
    }

    }
