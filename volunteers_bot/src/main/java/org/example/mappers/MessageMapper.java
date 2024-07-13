package org.example.mappers;

import org.example.pojo.dto.MessageDto;
import org.example.enums.EMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageMapper {
    public MessageDto messageDto(Message message, EMessage EMessage) {
        MessageDto messageDto = new MessageDto();
        messageDto.setData(message.getText());
        messageDto.setChatId(message.getChatId());
        messageDto.setUserMessageId(message.getMessageId());
        messageDto.setEMessage(EMessage);
        return messageDto;
    }

    public MessageDto messageDto(CallbackQuery callbackQuery) {
        MessageDto messageDto = new MessageDto();
        messageDto.setData(callbackQuery.getData());
        messageDto.setChatId(callbackQuery.getMessage().getChatId());
        messageDto.setEMessage(EMessage.CALLBACK);
        return messageDto;
    }
}
