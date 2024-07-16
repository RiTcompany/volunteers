package org.example.services;

import org.example.enums.EConversation;
import org.example.enums.EMessage;
import org.example.exceptions.ChatNotFoundException;
import org.example.exceptions.CommandException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ConversationService {
    void startConversation(long chatId, EConversation eConversation, AbsSender sender);

    void executeConversationStep(Update update, EMessage EMessage, AbsSender sender) throws CommandException;
}
