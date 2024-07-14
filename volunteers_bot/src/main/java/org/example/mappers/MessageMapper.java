package org.example.mappers;

import org.example.pojo.dto.MessageDto;
import org.example.enums.EMessage;
import org.example.pojo.entities.ChatHash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageMapper {
    public MessageDto messageDto(Update update, EMessage eMessage, ChatHash chatHash) {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(chatHash.getId());
        messageDto.setPrevBotMessageId(chatHash.getPrevBotMessageId());
        messageDto.setEMessage(eMessage);
        switch (eMessage) {
            case TEXT, COMMAND -> messageDto.setData(update.getMessage().getText());
            case CALLBACK -> messageDto.setData(update.getCallbackQuery().getData());
        }
        return messageDto;
    }
}
