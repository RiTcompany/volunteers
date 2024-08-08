package org.example.enums;

import lombok.Getter;

@Getter
public enum ECity {
    SPB("Санкт-Петербург"),
    OTHER("Другое");

    private final String value;

    ECity(String value) {
        this.value = value;
    }
}
