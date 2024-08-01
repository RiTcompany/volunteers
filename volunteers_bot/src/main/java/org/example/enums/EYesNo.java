package org.example.enums;

import lombok.Getter;

@Getter
public enum EYesNo {
    YES("ДА"),
    NO("НЕТ");

    private final String string;

    EYesNo(String string) {
        this.string = string;
    }
}
