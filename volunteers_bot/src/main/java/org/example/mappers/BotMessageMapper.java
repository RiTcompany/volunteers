package org.example.mappers;

import org.example.entities.BotMessage;
import org.example.enums.EBotMessage;
import org.springframework.stereotype.Component;

@Component
public class BotMessageMapper {
    public BotMessage botMessage(long writerId) {
        BotMessage botMessage = new BotMessage();
        botMessage.setWriterId(writerId);
        botMessage.setStatus(EBotMessage.WRITING);
        return botMessage;
    }
}
