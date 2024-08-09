package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.conversation.AConversation;
import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ConversationStepService;
import org.example.steps.ConversationStep;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationStepServiceImpl implements ConversationStepService {
    private final Map<EConversation, AConversation> conversationMap;
    private final Map<EConversationStep, ConversationStep> conversationStepMap;

    @Override
    public EConversationStep getStartStep(EConversation eConversation) {
        return conversationMap.get(eConversation).getStartStep();
    }

    @Override
    public void prepareStep(ChatHash chatHash, AbsSender sender) {
        ConversationStep step = getConversationStep(chatHash);
        if (step != null) {
            prepareStep(step, chatHash, sender);
        }
    }

    @Override
    public EConversationStep executeStep(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        ConversationStep step = getConversationStep(chatHash);
        int stepIndex = executeStep(step, chatHash, messageDto, sender);

        if (stepIndex == -1) {
            return chatHash.getEConversationStep();
        }

        return getNextEStep(chatHash, stepIndex);
    }

    @Override
    public String getFinishMessageText(EConversation eConversation) {
        return conversationMap.get(eConversation).getFinishMessageText();
    }

    private void prepareStep(ConversationStep step, ChatHash chatHash, AbsSender sender) {
        try {
            step.prepare(chatHash, sender);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chatHash.getId(), e.getUserMessage(), sender);
        }
    }

    private ConversationStep getConversationStep(ChatHash chatHash) {
        return conversationStepMap.get(chatHash.getEConversationStep());
    }

    private int executeStep(ConversationStep step, ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        try {
            return step.execute(chatHash, messageDto, sender);
        } catch (AbstractException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chatHash.getId(), e.getUserMessage(), sender);
        }

        return -1;
    }

    private EConversationStep getNextEStep(ChatHash chatHash, int stepIndex) {
        return conversationMap
                .get(chatHash.getEConversation())
                .getNextStep(chatHash.getEConversationStep(), stepIndex);

    }
}
