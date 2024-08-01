package org.example.enums;

import lombok.Getter;

@Getter
public enum EEducationInstitution {
    SCHOOL("SCHOOL"),
    UNIVERSITY("UNIVERSITY"),
    SECONDARY_PROFESSIONAL("SECONDARY_PROFESSIONAL");

    private final String string;

    EEducationInstitution(String string) {
        this.string = string;
    }
}
