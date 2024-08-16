package org.example.mappers;

import org.example.entities.Volunteer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class VolunteerMapper {
    public Volunteer volunteer(long chatId, String tgLink) {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        volunteer.setTgLink(tgLink);
        return volunteer;
    }

    public Volunteer volunteer(Volunteer volunteer, Message message) {
        volunteer.setTgLink(message.getChat().getUserName());
        return volunteer;
    }
}
