package org.example.commands;

import org.example.enums.EConversation;
import org.example.pojo.entities.Parent;
import org.example.repositories.ParentRepository;
import org.example.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class ParentRegisterCommand extends BotCommand  {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ParentRepository parentRepository;

    public ParentRegisterCommand() {
        super("parent_register", "Parent register command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        createParent(chat.getId());
        conversationService.startConversation(chat.getId(), EConversation.PARENT_REGISTER, absSender);
    } // TODO : добавить отказ в регистрации, если она уже пройдена

    private void createParent(long chatId) {
        if (!parentRepository.existsByChatId(chatId)) {
            Parent parent = new Parent(chatId);
            parentRepository.saveAndFlush(parent);
        }
    }
}
