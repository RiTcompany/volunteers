package org.example.enums;

import lombok.Getter;

@Getter
public enum EGender {
    MALE("Мужской"),
    FEMALE("Женский");

    private final String gender;

    EGender(String gender) {
        this.gender = gender;
    }
}
