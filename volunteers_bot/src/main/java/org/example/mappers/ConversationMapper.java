package org.example.mappers;

import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

@Component
public class ConversationMapper {
    public ChatHash conversationHash(
            long chatId,
            EConversation eConversation,
            EConversationStep eConversationStep,
            Integer lastMessageId
    ) {
        ChatHash chatHash = new ChatHash();
        chatHash.setId(chatId);
        chatHash.setEConversation(eConversation);
        chatHash.setEConversationStep(eConversationStep);
        chatHash.setLastMessageId(lastMessageId);
        return chatHash;
    }
}
