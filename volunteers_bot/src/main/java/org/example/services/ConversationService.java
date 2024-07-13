package org.example.services;

import org.example.enums.EConversation;
import org.example.enums.EMessage;
import org.example.exceptions.ChatNotFoundException;
import org.example.exceptions.CommandException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ConversationService {
    void executeConversationStep(Message message, EMessage EMessage, AbsSender sender) throws CommandException, ChatNotFoundException;

    void executeConversationStep(CallbackQuery callbackQuery, AbsSender sender) throws ChatNotFoundException;

    void startConversation(long chatId, EConversation eConversation, AbsSender sender);

    void stopConversation(long chatId);
}
