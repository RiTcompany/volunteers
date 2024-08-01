package org.example.services;

import org.example.entities.Moderator;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface ModeratorService {
    Moderator getModeratorByChatId(long chatId) throws EntityNotFoundException;
    boolean existsByChatId(long chatId);
}
