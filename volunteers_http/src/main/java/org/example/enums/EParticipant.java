package org.example.enums;

import lombok.Getter;

@Getter
public enum EParticipant {
    VOLUNTEER("Волонтёр");

    private final String value;

    EParticipant(String value) {
        this.value = value;
    }
}
