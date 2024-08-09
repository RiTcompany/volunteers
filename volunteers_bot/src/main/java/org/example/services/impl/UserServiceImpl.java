package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.BotUser;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.BotUserRepository;
import org.example.services.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final BotUserRepository botUserRepository;

    @Override
    public BotUser getByChatIdAndRole(long chatId, ERole eRole) throws EntityNotFoundException {
        BotUser botUser = botUserRepository.findByTgId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует %s с ID = %d".formatted(eRole, chatId)
                ));

        if (!hasRole(botUser, eRole)) {
            throw new EntityNotFoundException("Не существует %s с ID = %d".formatted(eRole, chatId));
        }

        return botUser;
    }

    @Override
    public boolean hasRole(BotUser botUser, ERole eRole) {
        return botUser.getRoleList().stream().anyMatch(role -> role.getRoleName().equals(eRole));
    }
}
