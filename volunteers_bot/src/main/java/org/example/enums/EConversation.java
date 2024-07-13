package org.example.enums;

import lombok.Getter;

@Getter
public enum EConversation {
    REGISTER(1);

    private final int conversationInt;

    EConversation(int conversationInt) {
        this.conversationInt = conversationInt;
    }
}
