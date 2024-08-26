package org.example.enums;

import lombok.Getter;

@Getter
public enum EAnorak {
    OVER_OUTWEAR("Поверх верхней одежды"),
    OVER_MAIN_CLOTHES("Поверх основной одежды");

    private final String value;

    EAnorak(String value) {
        this.value = value;
    }
}
