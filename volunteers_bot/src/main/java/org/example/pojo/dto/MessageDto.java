package org.example.pojo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.EMessage;

@Getter @Setter
public class MessageDto {
    private long chatId;
    private String data;
    private int prevBotMessageId;
    private EMessage EMessage;
}
