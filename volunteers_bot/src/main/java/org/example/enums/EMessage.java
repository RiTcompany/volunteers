package org.example.enums;

import lombok.Getter;

@Getter
public enum EMessage {
    TEXT("text"),
    COMMAND("command"),
    CALLBACK("callback");

    private final String description;

    EMessage(String description) {
        this.description = description;
    }

}
