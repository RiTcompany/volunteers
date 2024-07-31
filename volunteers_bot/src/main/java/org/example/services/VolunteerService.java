package org.example.services;

import org.example.entities.ChildDocument;
import org.example.exceptions.EntityNotFoundException;
import org.example.entities.Volunteer;
import org.springframework.stereotype.Service;

@Service
public interface VolunteerService {
    Volunteer getByChatId(long chatId) throws EntityNotFoundException;

    void saveAndFlush(Volunteer volunteer);

    void create(long chatId, String tgUserName);
}
