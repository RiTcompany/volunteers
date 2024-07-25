package org.example.mappers;

import org.example.pojo.dto.MessageDto;
import org.example.enums.EMessage;
import org.example.pojo.entities.ChatHash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Component
public class MessageMapper {
    public MessageDto messageDto(Update update, EMessage eMessage, ChatHash chatHash) {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(chatHash.getId());
        messageDto.setEMessage(eMessage);

        if (Objects.requireNonNull(eMessage) == EMessage.CALLBACK) {
            messageDto.setData(update.getCallbackQuery().getData());
        } else {
            messageDto.setData(update.getMessage().getText());
        }

        switch (eMessage) {
            case DOCUMENT -> messageDto.setDocument(update.getMessage().getDocument());
            case PHOTO -> messageDto.setPhotoList(update.getMessage().getPhoto());
        }

        return messageDto;
    }
}
