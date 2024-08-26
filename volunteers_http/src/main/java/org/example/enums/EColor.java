package org.example.enums;

import lombok.Getter;

@Getter
public enum EColor {
    RED("Красный"),
    YELLOW("Жёлтый"),
    GREEN("Зелёный"),
    NOT_FOUND("Not found");

    private final String value;

    EColor(String value) {
        this.value = value;
    }
}
