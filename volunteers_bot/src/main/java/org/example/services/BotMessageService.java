package org.example.services;

import org.example.entities.BotMessage;
import org.example.entities.BotMessageButton;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BotMessageService {
    void create(long botUserId);

    BotMessage getProcessedMessageByUserId(long botUserId) throws EntityNotFoundException;

    void saveText(long botUserId, String text) throws EntityNotFoundException;

    void saveSentStatus(BotMessage botMessage);

    void delete(BotMessage botMessage);

    void createButton(long botUserId, String buttonName, String buttonLink) throws EntityNotFoundException;

    List<BotMessageButton> getButtonList(BotMessage botMessage);

    void deleteButtons(BotMessage botMessage);
}
