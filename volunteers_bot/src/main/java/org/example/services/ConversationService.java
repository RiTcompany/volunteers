package org.example.services;

import org.example.enums.EConversation;
import org.example.enums.EMessage;
import org.example.exceptions.AbstractException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ConversationService {
    void startConversation(long chatId, EConversation eConversation, AbsSender sender) throws AbstractException;

    void executeConversationStep(Update update, EMessage EMessage, AbsSender sender) throws AbstractException;
}
