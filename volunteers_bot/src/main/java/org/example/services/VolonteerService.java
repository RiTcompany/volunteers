package org.example.services;

import org.example.pojo.entities.Volonteer;
import org.springframework.stereotype.Service;

@Service
public interface VolonteerService {
    Volonteer getVolonteerByChatId(long chatId);

    void saveAndFlushVolonteer(Volonteer volonteer);
}
