package com.api.costs.orcamento;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Categoria {

    CREDITO,
    DEBITO;

    @JsonCreator
    public static Categoria fromString(String value){
        return value == null ? null : Categoria.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue(){
        return name();
    }

}
