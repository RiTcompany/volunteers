package org.example.enums;

import lombok.Getter;

@Getter
public enum EGender {
    MALE("male"),
    FEMALE("female");

    private final String genderStr;

    EGender(String genderStr) {
        this.genderStr = genderStr;
    }
}
