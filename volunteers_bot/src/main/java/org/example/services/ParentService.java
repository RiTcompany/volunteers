package org.example.services;

import org.example.exceptions.EntityNotFoundException;
import org.example.entities.Parent;
import org.springframework.stereotype.Service;

@Service
public interface ParentService {
    Parent getByChatId(long chatId) throws EntityNotFoundException;

    void saveAndFlush(Parent parent);

    void create(long chatId);
}