package org.example.services;

import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ConversationStepService {
    EConversationStep getStartStep(EConversation eConversation);

    void prepareStep(ChatHash chatHash, AbsSender sender);

    EConversationStep executeStep(ChatHash chatHash, MessageDto messageDto, AbsSender sender);

    String getFinishMessageText(EConversation eConversation);
}
