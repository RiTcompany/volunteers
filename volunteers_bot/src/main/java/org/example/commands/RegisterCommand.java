package org.example.commands;

import org.example.enums.EConversation;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;
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
    @Autowired
    private VolonteerRepository volonteerRepository;
    private final EConversation CONVERSATION_TYPE = EConversation.REGISTER;
    private static final String TG_LINK_TEMPLATE = "https://t.me/";

    public RegisterCommand() {
        super("register", "Register command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        createVolonteer(chat.getId(), chat.getUserName());
        conversationService.startConversation(chat.getId(), CONVERSATION_TYPE, absSender);
    }

    private void createVolonteer(long chatId, String tgUserName) {
        if (!volonteerRepository.existsByChatId(chatId)) {
            Volonteer volonteer = new Volonteer(chatId, TG_LINK_TEMPLATE.concat(tgUserName));
            volonteerRepository.saveAndFlush(volonteer);
        }
    }
}
