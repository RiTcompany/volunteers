package org.example.enums;

import lombok.Getter;

@Getter
public enum EConversation {
    VOLUNTEER_REGISTER(1),
    PARENT_REGISTER(2);

    private final int conversationInt;

    EConversation(int conversationInt) {
        this.conversationInt = conversationInt;
    }
}
