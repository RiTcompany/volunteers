package org.example.services;

import org.example.exceptions.EntityNotFoundException;
import org.example.pojo.entities.Volunteer;
import org.springframework.stereotype.Service;

@Service
public interface VolunteerService {
    Volunteer getByChatId(long chatId) throws EntityNotFoundException;

    void saveAndFlush(Volunteer volunteer);
}
