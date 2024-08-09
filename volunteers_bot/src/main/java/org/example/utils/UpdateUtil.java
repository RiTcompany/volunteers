package org.example.utils;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateUtil {
    public static String getUserInputText(Update update) {
        if (isCallback(update)) {
            return update.getCallbackQuery().getData();
        } else {
            return update.getMessage().getText();
        }
    }

    public static boolean isCallback(Update update) {
        return update.hasCallbackQuery();
    }

    public static long getChatId(Update update) {
        return getChat(update).getId();
    }

    public static boolean isPrivateChat(Update update) {
        return getChat(update).isUserChat();
    }

    public static Chat getChat(Update update) {
        return getMessage(update).getChat();
    }

    public static Message getMessage(Update update) {
        return (update.hasCallbackQuery())
                ? update.getCallbackQuery().getMessage()
                : update.getMessage();
    }
}
