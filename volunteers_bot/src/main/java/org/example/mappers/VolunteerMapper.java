package org.example.mappers;

import org.example.entities.Volunteer;
import org.springframework.stereotype.Component;

@Component
public class VolunteerMapper {
    private static final String TG_LINK_TEMPLATE = "https://t.me/";

    public Volunteer volunteer(long chatId, String tgUserName) {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        volunteer.setTgLink(TG_LINK_TEMPLATE.concat(tgUserName));
        return volunteer;
    }
}
