package org.example.commands.register;

import org.example.enums.EConversation;
import org.example.services.ConversationService;
import org.example.services.VolunteerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class VolunteerRegisterCommand extends BotCommand {
    private final ConversationService conversationService;
    private final VolunteerService volunteerService;

    public VolunteerRegisterCommand(
            ConversationService conversationService, VolunteerService volunteerService
    ) {
        super("volunteer_register", "Volunteer register command");
        this.conversationService = conversationService;
        this.volunteerService = volunteerService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
//        if (volunteerService.isVolunteerExists(chat.getId())) {
//            MessageUtil.sendMessageText("Вы уже зарегистрированы", chat.getId(), absSender);
//        }
//        TODO : не забыть вернуть проверку на повторную регистрацию

        volunteerService.create(chat.getId(), chat.getUserName());
        conversationService.startConversation(chat.getId(), EConversation.VOLUNTEER_REGISTER, absSender);
    }
}
