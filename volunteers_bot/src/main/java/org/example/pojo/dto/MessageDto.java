package org.example.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.EMessage;
import org.telegram.telegrambots.meta.api.objects.Document;

@Getter @Setter
public class MessageDto {
    private long chatId;
    private String data;
    private Document document;
    private EMessage EMessage;
}
