package org.example.enums;

import lombok.Getter;

@Getter
public enum EPageMove {
    PREV("◀"),
    NEXT("▶");

    private final String string;

    EPageMove(String string) {
        this.string = string;
    }

}
