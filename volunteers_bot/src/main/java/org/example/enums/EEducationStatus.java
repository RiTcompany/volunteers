package org.example.enums;

import lombok.Getter;

@Getter
public enum EEducationStatus {
    STUDY_SCHOOL("Учусь в школе"),
    STUDY_UNIVERSITY("Учусь в ВУЗе"),
    STUDY_SECONDARY_PROFESSIONAL("Учусь в ОО СПО"),
    FINISHED_UNIVERSITY("Окончил ВУЗ"),
    FINISHED_SECONDARY_PROFESSIONAL("Окончил ОО СПО");

    private final String value;

    EEducationStatus(String value) {
        this.value = value;
    }
}
