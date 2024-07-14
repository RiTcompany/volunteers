package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.repositories.ConversationRepository;
import org.example.steps.ConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.services.ConversationStepService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConversationStepServiceImpl implements ConversationStepService {
    private final Map<EConversation, Map<EConversationStep, ConversationStep>> conversationMap;
    private final Map<EConversation, EConversationStep> conversationStartStepMap;

    public EConversationStep getStartStep(EConversation eConversation) {
        return conversationStartStepMap.get(eConversation);
    }

    public void prepareStep(ChatHash chatHash, AbsSender sender) {
        ConversationStep step = getConversationStep(chatHash);
        step.prepare(chatHash, sender);
    }

    public EConversationStep executeStep(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        ConversationStep step = getConversationStep(chatHash);
        return step.execute(chatHash, messageDto, sender);
    }

    private ConversationStep getConversationStep(ChatHash chatHash) {
        return conversationMap
                .get(chatHash.getEConversation())
                .get(chatHash.getEConversationStep());
    }
}
