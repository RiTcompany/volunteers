package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.enums.EMessage;
import org.example.exceptions.CommandException;
import org.example.mappers.ConversationMapper;
import org.example.mappers.MessageMapper;
import org.example.repositories.ConversationRepository;
import org.example.services.ConversationService;
import org.example.utils.ChatUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final ConversationMapper conversationMapper;
    private final ConversationStepServiceImpl conversationStepService;

    public void startConversation(long chatId, EConversation eConversation, AbsSender sender) {
        ChatHash chatHash = createChatHash(chatId, eConversation);
        prepareAndSaveStep(chatHash, sender);
    }

    public void executeConversationStep(Update update, EMessage eMessage, AbsSender sender) throws CommandException {
        long chatId = ChatUtil.getChatId(update, eMessage);
        ChatHash chatHash = getChat(chatId);
        if (chatHash != null) {
            MessageDto messageDto = messageMapper.messageDto(update, eMessage, chatHash);
            executeConversationStep(chatHash, messageDto, sender);
        }
    }

    private ChatHash getChat(long chatId) {
        return conversationRepository.findById(chatId).orElse(null);
    }

    private void executeConversationStep(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender
    ) throws CommandException {
        processCommandExistence(messageDto);
        EConversationStep nextStep = conversationStepService.executeStep(chatHash, messageDto, sender);
        if (isStepCompleted(nextStep, chatHash)) {
            setNextStep(chatHash, nextStep);
            prepareAndSaveStep(chatHash, sender);
        }
    }

    private boolean isStepCompleted(EConversationStep step, ChatHash chatHash) {
        return !chatHash.getEConversationStep().equals(step);
    }

    private void setNextStep(ChatHash chatHash, EConversationStep nextStep) {
        chatHash.setEConversationStep(nextStep);
    }

    private ChatHash createChatHash(long chatId, EConversation eConversation) {
        EConversationStep eStartStep = conversationStepService.getStartStep(eConversation);
        return conversationMapper.conversationHash(chatId, eConversation, eStartStep);
    }

    private void prepareAndSaveStep(ChatHash chatHash, AbsSender sender) {
        if (chatHash.getEConversationStep() != null) {
            conversationStepService.prepareStep(chatHash, sender);
            conversationRepository.save(chatHash);
        } else {
            conversationRepository.deleteById(chatHash.getId());
        }
    }

    private void processCommandExistence(MessageDto messageDto) throws CommandException {
        if (EMessage.COMMAND.equals(messageDto.getEMessage())) {
            throw new CommandException(
                    "Trying to use command in command",
                    "Вы не можете ввести другую команду, пока не завершите данный диалог"
            );
        }
    }
}
