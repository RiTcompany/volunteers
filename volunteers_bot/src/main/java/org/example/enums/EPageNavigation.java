package org.example.enums;

import lombok.Getter;

@Getter
public enum EPageNavigation {
    PREV("◀"),
    NEXT("▶");

    private final String description;

    EPageNavigation(String description) {
        this.description = description;
    }

}
