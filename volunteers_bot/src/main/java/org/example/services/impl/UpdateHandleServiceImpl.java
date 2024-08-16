package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EMessage;
import org.example.exceptions.AbstractException;
import org.example.exceptions.CommandException;
import org.example.services.ConversationService;
import org.example.services.GroupChatService;
import org.example.services.UpdateHandleService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateHandleServiceImpl implements UpdateHandleService {
    private static final String EXCEPTION_MESSAGE = "Такой команды не существует";
    private final CommandRegistry commandRegistry;
    private final ConversationService conversationService;
    private final GroupChatService groupChatService;

    @Override
    public void handleMyChatMember(Update update) {
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        if (groupChatService.isBotNewChatMember(chatMemberUpdated)) {
            groupChatService.save(chatMemberUpdated);
        } else if (groupChatService.isBotKickedChatMember(chatMemberUpdated)) {
            groupChatService.delete(chatMemberUpdated);
        }
    }

    @Override
    public void handleCallbackRequest(Update update, AbsSender sender) throws AbstractException {
        conversationService.executeConversationStep(update, EMessage.CALLBACK, sender);
    }

    @Override
    public void handleMessageRequest(Update update, AbsSender sender) throws AbstractException {
        Message message = update.getMessage();
        if (message.isCommand()) {
            executeCommand(update, sender);
        } else if (message.hasDocument()) {
            conversationService.executeConversationStep(update, EMessage.DOCUMENT, sender);
        } else if (message.hasPhoto()) {
            conversationService.executeConversationStep(update, EMessage.PHOTO, sender);
        } else if (message.hasText()) {
            conversationService.executeConversationStep(update, EMessage.TEXT, sender);
        }
    }

    private void executeCommand(Update update, AbsSender sender) throws AbstractException {
        Message message = update.getMessage();
        conversationService.executeConversationStep(update, EMessage.COMMAND, sender);
        if (!commandRegistry.executeCommand(sender, message)) {
            throw new CommandException(EXCEPTION_MESSAGE, EXCEPTION_MESSAGE);
        }
    }
}
