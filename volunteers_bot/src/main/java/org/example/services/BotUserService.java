package org.example.services;

import org.example.entities.BotUser;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface BotUserService {
    BotUser getByChatIdAndRole(long chatId, ERole eRole) throws EntityNotFoundException;

    boolean hasRole(BotUser botUser, ERole eRole);

    boolean existsByTgId(long chatId);
}
