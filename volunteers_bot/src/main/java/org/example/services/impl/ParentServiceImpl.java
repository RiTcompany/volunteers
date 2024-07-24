package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.pojo.entities.Parent;
import org.example.repositories.ParentRepository;
import org.example.services.ParentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;

    @Override
    public Parent getByChatId(long chatId) {
        return parentRepository.findByChatId(chatId).orElseGet(() -> new Parent(chatId));
    }

    @Override
    public void saveAndFlush(Parent parent) {
        parentRepository.saveAndFlush(parent);
    }
}
