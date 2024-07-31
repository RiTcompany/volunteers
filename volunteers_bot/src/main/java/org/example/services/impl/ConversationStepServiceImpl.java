package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.EntityNotFoundException;
import org.example.steps.ConversationStep;
import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.services.ConversationStepService;
import org.example.utils.MessageUtil;
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
            prepareStep(step, chatHash, sender);
        }
    }

    public EConversationStep executeStep(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        ConversationStep step = getConversationStep(chatHash);
        int stepIndex = executeStep(step, chatHash, messageDto, sender);

        if (stepIndex == -1) {
            return chatHash.getEConversationStep();
        }

        return getNextEStep(chatHash, stepIndex);
    }

    private void prepareStep(ConversationStep step, ChatHash chatHash, AbsSender sender) {
        try {
            step.prepare(chatHash, sender);
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText(e.getUserMessage(), chatHash.getId(), sender);
        }
    }

    private ConversationStep getConversationStep(ChatHash chatHash) {
        return conversationStepMap.get(chatHash.getEConversationStep());
    }

    private int executeStep(ConversationStep step, ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        try {
            return step.execute(chatHash, messageDto, sender);
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText(e.getUserMessage(), chatHash.getId(), sender);
        }

        return -1;
    }

    private EConversationStep getNextEStep(ChatHash chatHash, int stepIndex) {
        return conversationStepGraph
                .get(chatHash.getEConversation())
                .get(chatHash.getEConversationStep())
                .get(stepIndex);
    }
}
