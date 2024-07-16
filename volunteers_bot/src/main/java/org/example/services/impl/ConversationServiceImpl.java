package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.enums.EMessage;
import org.example.exceptions.CommandException;
import org.example.mappers.ChatHashMapper;
import org.example.mappers.MessageMapper;
import org.example.repositories.ChatHashRepository;
import org.example.services.ConversationService;
import org.example.utils.ChatUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationStepServiceImpl conversationStepService;
    private final ChatHashRepository chatHashRepository;
    private final MessageMapper messageMapper;
    private final ChatHashMapper chatHashMapper;

    public void startConversation(long chatId, EConversation eConversation, AbsSender sender) {
        ChatHash chatHash = createChatHash(chatId, eConversation);
        prepareStep(chatHash, sender);
        saveChatHash(chatHash);
    }

    public void executeConversationStep(Update update, EMessage eMessage, AbsSender sender) throws CommandException {
        long chatId = ChatUtil.getChatId(update, eMessage);
        ChatHash chatHash = getChatById(chatId);
        if (chatHash != null) {
            MessageDto messageDto = messageMapper.messageDto(update, eMessage, chatHash);
            executeConversationStep(chatHash, messageDto, sender);
        }
    }

    private ChatHash getChatById(long chatId) {
        return chatHashRepository.findById(chatId).orElse(null);
    }

    private void executeConversationStep(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws CommandException {
        handleCommand(messageDto);

        EConversationStep prevStep = chatHash.getEConversationStep();
        EConversationStep nextStep = executeStep(chatHash, messageDto, sender);

        if (isStepCompleted(nextStep, prevStep)) {
            setNextStep(chatHash, nextStep);
            prepareStep(chatHash, sender);
        }

        saveChatHash(chatHash);
        handleConversationEnd(chatHash);
    }

    private EConversationStep executeStep(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        return conversationStepService.executeStep(chatHash, messageDto, sender);
    }

    private boolean isStepCompleted(EConversationStep nextStep, EConversationStep prevStep) {
        return !prevStep.equals(nextStep);
    }

    private void setNextStep(ChatHash chatHash, EConversationStep nextStep) {
        chatHash.setEConversationStep(nextStep);
    }

    private ChatHash createChatHash(long chatId, EConversation eConversation) {
        ChatHash chatHash = chatHashMapper.chatHash(chatId, eConversation);
        setNextStep(chatHash, conversationStepService.getStartStep(eConversation));
        return chatHash;
    }

    private void prepareStep(ChatHash chatHash, AbsSender sender) {
        conversationStepService.prepareStep(chatHash, sender);
    }

    private void handleConversationEnd(ChatHash chatHash) {
        if (isConversationFinished(chatHash)) {
            deleteChatHash(chatHash);
        }
    }

    private boolean isConversationFinished(ChatHash chatHash) {
        return chatHash.getEConversationStep() == null;
    }

    private void saveChatHash(ChatHash chatHash) {
        chatHashRepository.save(chatHash);
    }

    private void deleteChatHash(ChatHash chatHash) {
        chatHashRepository.deleteById(chatHash.getId());
    }

    private void handleCommand(MessageDto messageDto) throws CommandException {
        if (EMessage.COMMAND.equals(messageDto.getEMessage())) {
            throw new CommandException(
                    "Trying to use command in command",
                    "Вы не можете ввести другую команду, пока не завершите данный диалог"
            );
        }
    }
}
