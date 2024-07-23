package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.steps.ConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.services.ConversationStepService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConversationStepServiceImpl implements ConversationStepService {
    private final Map<EConversation, Map<EConversationStep, List<EConversationStep>>> conversationStepGraph;
    private final Map<EConversation, EConversationStep> conversationStartStepMap;
    private final Map<EConversationStep, ConversationStep> conversationStepMap;

    public EConversationStep getStartStep(EConversation eConversation) {
        return conversationStartStepMap.get(eConversation);
    }

    public void prepareStep(ChatHash chatHash, AbsSender sender) {
        ConversationStep step = getConversationStep(chatHash);
        if (step != null) {
            step.prepare(chatHash, sender);
        }
    }

    public EConversationStep executeStep(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        ConversationStep step = getConversationStep(chatHash);
        int stepIndex = step.execute(chatHash, messageDto, sender);
        if (stepIndex == -1) {
            return chatHash.getEConversationStep();
        }

        return conversationStepGraph
                .get(chatHash.getEConversation())
                .get(chatHash.getEConversationStep())
                .get(stepIndex);
    }

    private ConversationStep getConversationStep(ChatHash chatHash) {
        return conversationStepMap.get(chatHash.getEConversationStep());
    }
}
