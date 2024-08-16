package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.conversation.AConversation;
import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.exceptions.AbstractException;
import org.example.services.ConversationStepService;
import org.example.steps.ConversationStep;
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
    public void prepareStep(ChatHash chatHash, AbsSender sender) throws AbstractException {
        ConversationStep step = getConversationStep(chatHash);
        step.prepare(chatHash, sender);
    }

    @Override
    public EConversationStep executeStep(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender
    ) throws AbstractException {
        ConversationStep step = getConversationStep(chatHash);
        int stepIndex = step.execute(chatHash, messageDto, sender);
        if (stepIndex == -1) {
            return chatHash.getEConversationStep();
        }

        return getNextEStep(chatHash, stepIndex);
    }

    @Override
    public String getFinishMessageText(EConversation eConversation) {
        return conversationMap.get(eConversation).getFinishMessageText();
    }

    private ConversationStep getConversationStep(ChatHash chatHash) {
        return conversationStepMap.get(chatHash.getEConversationStep());
    }

    private EConversationStep getNextEStep(ChatHash chatHash, int stepIndex) {
        return conversationMap
                .get(chatHash.getEConversation())
                .getNextStep(chatHash.getEConversationStep(), stepIndex);

    }
}
