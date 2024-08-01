package org.example.services;

import org.springframework.stereotype.Service;

@Service
public interface BotMessageService {
    void create(long writerId);
}
