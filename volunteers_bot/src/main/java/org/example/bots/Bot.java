package org.example.bots;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EMessage;
import org.example.exceptions.AbstractException;
import org.example.exceptions.ChatNotFoundException;
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
public final class Bot extends TelegramLongPollingBot {
    @Getter
    private final String botUsername;
    @Autowired
    private CommandRegistry commandRegistry;
    @Autowired
    private ConversationService conversationService;

    public Bot(String botUsername, String token) {
        super(token);
        this.botUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            executeMessageRequest(update);
        } else if (update.hasCallbackQuery()) {
            executeCallbackRequest(update);
        }
    }

    private void executeMessageRequest(Update update) {
        Message message = update.getMessage();
        try {
            if (message.isCommand()) {
                executeCommand(message);
            } else if (message.hasText()) {
                conversationService.executeConversationStep(message, EMessage.TEXT, this);
            }
        } catch (AbstractException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessage(e.getUserMessage(), message.getChatId(), this);
        }
    }

    private void executeCommand(Message message) throws CommandException, ChatNotFoundException {
        conversationService.executeConversationStep(message, EMessage.COMMAND, this);
        if (!commandRegistry.executeCommand(this, message)) {
            MessageUtil.sendMessage("Такой команды не существует", message.getChatId(), this);
        }
    }

    private void executeCallbackRequest(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        try {
            conversationService.executeConversationStep(callbackQuery, this);
        } catch (AbstractException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessage(e.getUserMessage(), callbackQuery.getMessage().getChatId(), this);
        }
    }
}
