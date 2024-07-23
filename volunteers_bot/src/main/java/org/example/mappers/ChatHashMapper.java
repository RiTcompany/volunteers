package org.example.mappers;

import org.example.enums.EConversation;
import org.example.pojo.entities.ChatHash;
import org.springframework.stereotype.Component;

@Component
public class ChatHashMapper {
    private static final int DEFAULT_MESSAGE_ID = -1;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    public ChatHash chatHash(long chatId, EConversation eConversation) {
        ChatHash chatHash = new ChatHash();
        chatHash.setId(chatId);
        chatHash.setEConversation(eConversation);
        chatHash.setPrevBotMessageId(DEFAULT_MESSAGE_ID);
        chatHash.setPrevBotMessagePageNumber(DEFAULT_PAGE_NUMBER);
        return chatHash;
    }
}
