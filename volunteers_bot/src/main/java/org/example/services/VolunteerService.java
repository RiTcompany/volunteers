package org.example.services;

import org.example.pojo.entities.Volunteer;
import org.springframework.stereotype.Service;

@Service
public interface VolunteerService {
    Volunteer getVolunteerByChatId(long chatId);

    void saveAndFlushVolunteer(Volunteer volunteer);
}
