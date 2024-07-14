package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.enums.EMessage;
import org.example.exceptions.ChatNotFoundException;
import org.example.exceptions.CommandException;
import org.example.mappers.ConversationMapper;
import org.example.mappers.MessageMapper;
import org.example.repositories.ConversationRepository;
import org.example.services.ConversationService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final ConversationMapper conversationMapper;
    private final ConversationStepServiceImpl conversationStepService;

    public void executeConversationStep(Message message, EMessage eMessage, AbsSender sender) throws CommandException, ChatNotFoundException {
        if (existsChat(message.getChatId())) {
            MessageDto messageDto = messageMapper.messageDto(message, eMessage);
            processCommandExistence(messageDto);
            executeConversationStep(messageDto, sender);
        }
    }

    public void executeConversationStep(CallbackQuery callbackQuery, AbsSender sender) throws ChatNotFoundException {
        if (existsChat(callbackQuery.getMessage().getChatId())) {
            MessageDto messageDto = messageMapper.messageDto(callbackQuery);
            executeConversationStep(messageDto, sender);
        }
    }

    public void startConversation(long chatId, EConversation eConversation, AbsSender sender) {
        ChatHash chatHash = conversationMapper.conversationHash(
                chatId, eConversation, conversationStepService.getStartStep(eConversation), -1
        );
        conversationRepository.save(chatHash);
        conversationStepService.prepareStep(chatHash, sender);
    }

    public void stopConversation(long chatId) {
        conversationRepository.deleteById(chatId);
    }

    private void executeConversationStep(MessageDto messageDto, AbsSender absSender) throws ChatNotFoundException {
        ChatHash chatHash = getConversationHash(messageDto.getChatId());
        EConversationStep nextStep = conversationStepService.executeStep(chatHash, messageDto, absSender);
        if (stepCompleted(nextStep, chatHash)) {
            saveAndPrepareNewNextStep(chatHash, nextStep, absSender);
        }
    }

    private boolean existsChat(long chatId) { // TODO : delete
        return conversationRepository.findById(chatId).isPresent();
    }

    private ChatHash getConversationHash(long chatId) throws ChatNotFoundException {
        return conversationRepository
                .findById(chatId)
                .orElseThrow(() -> throwChatNotFoundException(chatId));
    }

    private ChatNotFoundException throwChatNotFoundException(long chatId) {
        return new ChatNotFoundException("No chat with id = ".concat(String.valueOf(chatId)), ""); // TODO : возможно надо как-то иначе обработать эту ошибку, так как косяк на стороне сервера, а не пользователя
    }

    private boolean stepCompleted(EConversationStep step, ChatHash chatHash) {
        return !chatHash.getEConversationStep().equals(step);
    }

    private void saveAndPrepareNewNextStep(ChatHash chatHash, EConversationStep nextStep, AbsSender sender) {
        chatHash.setEConversationStep(nextStep);
        conversationRepository.save(chatHash);
        if (nextStep != null) conversationStepService.prepareStep(chatHash, sender);
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
