package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.BotUser;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.BotUserRepository;
import org.example.services.BotUserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotUserServiceImpl implements BotUserService {
    private final BotUserRepository botUserRepository;

    @Override
    public BotUser getByChatIdAndRole(
            long chatId, ERole eRole
    ) throws EntityNotFoundException {
        BotUser botUser = botUserRepository.findByTgId(chatId)
                .orElseThrow(() -> getException(eRole, chatId));

        if (!hasRole(botUser, eRole)) {
            throw getException(eRole, chatId);
        }

        return botUser;
    }

    @Override
    public boolean hasRole(BotUser botUser, ERole eRole) {
        return botUser.getRoleList().stream().anyMatch(role -> role.getRoleName().equals(eRole));
    }

    @Override
    public boolean existsByTgId(long chatId) {
        return botUserRepository.existsByTgId(chatId);
    }

    private EntityNotFoundException getException(ERole eRole, long chatId) {
        return new EntityNotFoundException("Не существует %s с ID = %d".formatted(eRole, chatId));
    }
}
