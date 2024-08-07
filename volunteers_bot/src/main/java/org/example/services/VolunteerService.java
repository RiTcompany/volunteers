package org.example.services;

import org.example.entities.Volunteer;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VolunteerService {
    Volunteer getByChatId(long chatId) throws EntityNotFoundException;

    void saveAndFlush(Volunteer volunteer);

    void create(long chatId, String tgUserName);

    List<Long> getVolunteerChatIdList();
}
