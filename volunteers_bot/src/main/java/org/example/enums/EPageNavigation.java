package org.example.enums;

import lombok.Getter;

@Getter
public enum EPageNavigation {
    PREV("◀"),
    NEXT("▶");

    private final String string;

    EPageNavigation(String string) {
        this.string = string;
    }

}
