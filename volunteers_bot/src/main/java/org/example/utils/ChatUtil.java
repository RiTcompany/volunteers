package org.example.utils;

import org.example.enums.EMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatUtil {
    public static long getChatId(Update update, EMessage eMessage) {
        return switch (eMessage) {
            case TEXT, COMMAND -> update.getMessage().getChatId();
            case CALLBACK -> update.getCallbackQuery().getMessage().getChatId();
        };
    }
}
