package org.example.commands;

import org.example.enums.EConversation;
import org.example.pojo.entities.Volunteer;
import org.example.repositories.VolunteerRepository;
import org.example.services.ConversationService;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class VolunteerRegisterCommand extends BotCommand {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private VolunteerRepository volunteerRepository;
    private static final String TG_LINK_TEMPLATE = "https://t.me/";

    public VolunteerRegisterCommand() {
        super("volunteer_register", "Volunteer register command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
//        if (volunteerRepository.existsByChatId(chat.getId())) {
//            MessageUtil.sendMessageText("Вы уже зарегистрированы", chat.getId(), absSender);
//        }

        createVolunteer(chat.getId(), chat.getUserName());
        conversationService.startConversation(chat.getId(), EConversation.VOLUNTEER_REGISTER, absSender);
    }

    private void createVolunteer(long chatId, String tgUserName) {
        if (!volunteerRepository.existsByChatId(chatId)) {
            Volunteer volunteer = new Volunteer(chatId, TG_LINK_TEMPLATE.concat(tgUserName));
            volunteerRepository.saveAndFlush(volunteer);
        }
    }
}
