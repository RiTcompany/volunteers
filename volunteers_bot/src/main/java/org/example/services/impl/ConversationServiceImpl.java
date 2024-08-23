package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.enums.EMessage;
import org.example.exceptions.AbstractException;
import org.example.exceptions.CommandException;
import org.example.mappers.MessageMapper;
import org.example.services.ChatHashService;
import org.example.services.ConversationService;
import org.example.services.ConversationStepService;
import org.example.utils.LogUtil;
import org.example.utils.MessageUtil;
import org.example.utils.UpdateUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationStepService conversationStepService;
    private final ChatHashService chatHashService;
    private final MessageMapper messageMapper;

    @Override
    public void startConversation(
            long chatId, EConversation eConversation, AbsSender sender
    ) throws AbstractException {
        ChatHash chatHash = createChatHash(chatId, eConversation);
        conversationStepService.prepareStep(chatHash, sender);
        chatHashService.save(chatHash);
    }

    @Override
    public void executeConversationStep(
            Update update, EMessage eMessage, AbsSender sender
    ) throws AbstractException {
        long chatId = UpdateUtil.getChatId(update);
        ChatHash chatHash = chatHashService.getChatById(chatId);
        log.info(LogUtil.getConversationLog(update, eMessage, chatHash));

        if (chatHash != null) {
            MessageDto messageDto = messageMapper.messageDto(update, eMessage, chatHash);
            executeConversationStep(chatHash, messageDto, sender);
        }
    }

    private void executeConversationStep(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender
    ) throws AbstractException {
        handleCommand(messageDto);

        EConversationStep prevStep = chatHash.getEConversationStep();
        EConversationStep nextStep = conversationStepService.executeStep(chatHash, messageDto, sender);

        if (nextStep == null) {
            handleConversationEnd(chatHash, sender);
            return;
        }

        if (isStepCompleted(nextStep, prevStep)) {
            chatHash.setEConversationStep(nextStep);
            conversationStepService.prepareStep(chatHash, sender);
        }

        chatHashService.save(chatHash);
    }

    private boolean isStepCompleted(EConversationStep nextStep, EConversationStep prevStep) {
        return !prevStep.equals(nextStep);
    }

    private ChatHash createChatHash(long chatId, EConversation eConversation) {
        EConversationStep startStep = conversationStepService.getStartStep(eConversation);
        return chatHashService.createChatHash(chatId, eConversation, startStep);
    }

    private void handleConversationEnd(ChatHash chatHash, AbsSender sender) {
        String finishMessageText = conversationStepService.getFinishMessageText(
                chatHash.getEConversation()
        );
        if (finishMessageText != null) {
            MessageUtil.sendMessageText(chatHash.getId(), finishMessageText, sender);
        }

        chatHashService.delete(chatHash);
    }

    private void handleCommand(MessageDto messageDto) throws CommandException {
        if (EMessage.COMMAND.equals(messageDto.getEMessage())) {
            throw new CommandException(
                    "Попытка вызвать команду во время действующего диалога",
                    "Вы не можете ввести другую команду, пока не завершите данный диалог"
            );
        }
    }
}
