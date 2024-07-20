package org.example.utils;

import org.example.enums.EMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatUtil {
    public static long getChatId(Update update, EMessage eMessage) {
        return (EMessage.CALLBACK.equals(eMessage))
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
    }
}
