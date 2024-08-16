package org.example.services;

import org.example.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Service;

@Service
public interface ChatHashService {
    ChatHash getChatById(long chatId);

    ChatHash createChatHash(
            long chatId, EConversation eConversation, EConversationStep eConversationStep
    );

    void save(ChatHash chatHash);

    void delete(ChatHash chatHash);
}
