package org.example.services;

import org.example.exceptions.AbstractException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface UpdateHandleService {
    void handleMyChatMember(Update update);

    void handleCallbackRequest(Update update, AbsSender sender) throws AbstractException;

    void handleMessageRequest(Update update, AbsSender sender) throws AbstractException;
}
