package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UniteTemps {
    Ans,
    Mois;

    @JsonCreator
    public static UniteTemps fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return UniteTemps.valueOf(value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
