package org.example.enums;

import lombok.Getter;

@Getter
public enum EGender {
    MALE("Мужской"),
    FEMALE("Женский");

    private final String string;

    EGender(String string) {
        this.string = string;
    }
}
