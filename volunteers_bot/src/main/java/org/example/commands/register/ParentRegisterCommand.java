package org.example.commands.register;

import org.example.enums.EConversation;
import org.example.services.ConversationService;
import org.example.services.ParentService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class ParentRegisterCommand extends BotCommand {
    private final ConversationService conversationService;
    private final ParentService parentService;

    public ParentRegisterCommand(
            ConversationService conversationService, ParentService parentService
    ) {
        super("parent_register", "Parent register command");
        this.conversationService = conversationService;
        this.parentService = parentService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
//        if (parentService.exists(chat.getId())) {
//            MessageUtil.sendMessageText("Вы уже зарегистрированы", chat.getId(), absSender);
//        }
//        TODO : не забыть вернуть проверку на повторную регистрацию

        parentService.create(chat.getId());
        conversationService.startConversation(chat.getId(), EConversation.PARENT_REGISTER, absSender);
    }
}
