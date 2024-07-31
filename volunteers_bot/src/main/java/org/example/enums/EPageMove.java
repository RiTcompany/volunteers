package org.example.enums;

import lombok.Getter;

@Getter
public enum EPageMove {
    PREV("◀"),
    NEXT("▶");

    private final String description;

    EPageMove(String description) {
        this.description = description;
    }

}
