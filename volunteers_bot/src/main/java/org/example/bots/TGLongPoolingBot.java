package org.example.bots;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EMessage;
import org.example.exceptions.AbstractException;
import org.example.exceptions.CommandException;
import org.example.services.ConversationService;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public final class TGLongPoolingBot extends TelegramLongPollingBot {
    @Getter
    private final String botUsername;
    @Autowired
    private CommandRegistry commandRegistry;
    @Autowired
    private ConversationService conversationService;

    public TGLongPoolingBot(String botUsername, String token) {
        super(token);
        this.botUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            executeCallbackRequest(update);
        } else if (update.hasMessage()) {
            executeMessageRequest(update);
        }
    }

    private void executeCallbackRequest(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        try {
            executeConversationStep(update, EMessage.CALLBACK);
        } catch (AbstractException e) {
            log.error(e.getMessage());
            sendExceptionMessage(callbackQuery.getMessage().getChatId(), e.getUserMessage());
        }
    }

    private void executeMessageRequest(Update update) {
        Message message = update.getMessage();
        try {
            if (message.isCommand()) {
                executeCommand(update);
            } else if (message.hasDocument()) {
                executeConversationStep(update, EMessage.DOCUMENT);
            } else if (message.hasPhoto()) {
                executeConversationStep(update, EMessage.PHOTO);
            } else if (message.hasText()) {
                executeConversationStep(update, EMessage.TEXT);
            }
        } catch (AbstractException e) {
            log.error(e.getMessage());
            sendExceptionMessage(message.getChatId(), e.getUserMessage());
        }
    }

    private void executeCommand(Update update) throws CommandException {
        Message message = update.getMessage();
        executeConversationStep(update, EMessage.COMMAND);
        if (!commandRegistry.executeCommand(this, message)) {
            sendExceptionMessage(message.getChatId(), "Такой команды не существует");
        }
    }

    private void executeConversationStep(Update update, EMessage eMessage) throws CommandException {
        conversationService.executeConversationStep(update, eMessage, this);
    }

    private void sendExceptionMessage(long chatId, String message) {
        MessageUtil.sendMessageText(message, chatId, this);
    }
}
