package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.mappers.ChatHashMapper;
import org.example.repositories.ChatHashRepository;
import org.example.services.ChatHashService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatHashServiceImpl implements ChatHashService {
    private final ChatHashRepository chatHashRepository;
    private final ChatHashMapper chatHashMapper;

    @Override
    public ChatHash getChatById(long chatId) {
        return chatHashRepository.findById(chatId).orElse(null);
    }

    @Override
    public ChatHash createChatHash(
            long chatId, EConversation eConversation, EConversationStep eConversationStep
    ) {
        return chatHashMapper.chatHash(chatId, eConversation, eConversationStep);
    }

    @Override
    public void save(ChatHash chatHash) {
        chatHashRepository.save(chatHash);
    }

    @Override
    public void delete(ChatHash chatHash) {
        chatHashRepository.deleteById(chatHash.getId());
    }
}
