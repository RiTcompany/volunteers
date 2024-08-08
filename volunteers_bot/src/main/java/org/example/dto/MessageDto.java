package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.EMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

@Getter
@Setter
public class MessageDto {
    private long chatId;
    private String data;
    private Document document;
    private List<PhotoSize> photoList;
    private EMessage EMessage;
}
