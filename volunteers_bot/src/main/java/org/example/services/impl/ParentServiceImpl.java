package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.ParentMapper;
import org.example.entities.Parent;
import org.example.repositories.ParentRepository;
import org.example.services.ParentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;

    @Override
    public Parent getByChatId(long chatId) throws EntityNotFoundException {
        return parentRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Не существует родителя с чатом ID = ".concat(String.valueOf(chatId)),
                "Что-то пошло не так, пожалуйста обратитесь в поддержку"
        ));
    }

    @Override
    public void saveAndFlush(Parent parent) {
        parentRepository.saveAndFlush(parent);
    }

    @Override
    public void create(long chatId) {
        if (!parentRepository.existsByChatId(chatId)) {
            parentRepository.saveAndFlush(parentMapper.parent(chatId));
        }
    }
}
