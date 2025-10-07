package com.api.costs.orcamento;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    PENDENTE,
    ABATIDO,
    EXPIRADO;

    @JsonCreator
    public static Status fromString(String value){
        return value == null ? null : Status.fromString(value.toUpperCase());
    }

    @JsonValue
    public String toValue(){
        return name();
    }

}
