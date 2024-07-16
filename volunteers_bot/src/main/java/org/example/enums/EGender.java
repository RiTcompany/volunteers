package org.example.enums;

import lombok.Getter;

@Getter
public enum EGender {
    MALE("Мужской"),
    FEMALE("Женский");

    private final String genderStr;

    EGender(String genderStr) {
        this.genderStr = genderStr;
    }
}
