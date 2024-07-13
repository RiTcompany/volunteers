package org.example.commands;

import org.example.enums.EConversation;
import org.example.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class RegisterCommand extends BotCommand {
    @Autowired
    private ConversationService conversationService;
    private final EConversation CONVERSATION_TYPE = EConversation.REGISTER;

    public RegisterCommand() {
        super("register", "Register command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        conversationService.startConversation(chat.getId(), CONVERSATION_TYPE, absSender);
    }
}
