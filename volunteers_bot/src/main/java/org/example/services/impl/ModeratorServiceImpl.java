package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Moderator;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.ModeratorRepository;
import org.example.services.ModeratorService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModeratorServiceImpl implements ModeratorService {
    private final ModeratorRepository moderatorRepository;

    @Override
    public Moderator getModeratorByChatId(long chatId) throws EntityNotFoundException {
        return moderatorRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует модератора с ID = ".concat(String.valueOf(chatId)),
                        "Что-то пошло не так, пожалуйста обратитесь в поддержку"
                ));
    }

    @Override
    public boolean existsByChatId(long chatId) {
        return moderatorRepository.existsByChatId(chatId);
    }
}
