package org.example.pojo.dto;

import lombok.Data;
import org.example.enums.EMessage;

@Data
public class MessageDto {
    private long chatId;
    private String data;
    private int prevBotMessageId;
    private EMessage EMessage;
}
