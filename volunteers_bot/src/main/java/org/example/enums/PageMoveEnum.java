package org.example.enums;

import lombok.Getter;

@Getter
public enum PageMoveEnum {
    PREV("◀"),
    NEXT("▶");

    private final String description;

    PageMoveEnum(String description) {
        this.description = description;
    }

}
