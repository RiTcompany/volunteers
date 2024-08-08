package org.example.enums;

import lombok.Getter;

@Getter
public enum EGender {
    MALE("Мужской"),
    FEMALE("Женский");

    private final String value;

    EGender(String value) {
        this.value = value;
    }
}
