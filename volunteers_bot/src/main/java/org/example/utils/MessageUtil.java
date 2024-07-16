package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

// TODO : Можно переделать под singleton и внести сюда бота
@Slf4j
public class MessageUtil {
    public static int sendMessage(String text, long chatId, AbsSender sender) {
        SendMessage message = completeSendMessage(text, String.valueOf(chatId));
        return sendMessage(message, sender);
    }

    public static int sendMessage(SendMessage message, AbsSender sender) {
        try {
            message.enableHtml(true);
            return sender.execute(message).getMessageId();
        } catch (TelegramApiException e) {
            log.error(
                    "Для ID чата {} не удалось отправить сообщение. Причина: {}",
                    message.getChatId(), e.getMessage()
            );
        }

        return -1;
    }

    public static void editMessage(long chatId, Integer messageId, String newText, AbsSender sender) {
        if (messageId > 0) {
            EditMessageText edit = completeEditMessageText(chatId, messageId, newText);
            editMessage(edit, sender);
        }
    }

    public static void editMessage(EditMessageText edit, AbsSender sender) {
        try {
            edit.enableHtml(true);
            sender.execute(edit);
        } catch (TelegramApiException e) {
            log.error(
                    "Для ID чата {} не удалось изменить сообщение. Причина: {}",
                    edit.getChatId(), e.getMessage()
            );
        }
    }

    public static SendMessage completeSendMessage(
            String text, String chatId, InlineKeyboardMarkup inlineKeyboardMarkup
    ) {
        SendMessage message = completeSendMessage(text, chatId);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public static SendMessage completeSendMessage(String text, String chatId) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        return message;
    }



    public static EditMessageReplyMarkup completeEditMessageReplyMarkup(
            String chatId, Integer messageId, InlineKeyboardMarkup inlineKeyboardMarkup
    ) {
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setMessageId(messageId);
        edit.setReplyMarkup(inlineKeyboardMarkup);
        edit.setChatId(chatId);
        return edit;
    }

    public static void editMessageReplyMarkup(EditMessageReplyMarkup edit, AbsSender sender) {
        try {
            sender.execute(edit);
        } catch (TelegramApiException e) {
            log.error(
                    "Для ID чата {} не удалось изменить сообщение. Причина: {}",
                    edit.getChatId(), e.getMessage()
            );
        }
    }

    private static EditMessageText completeEditMessageText(
            long chatId, Integer messageId, String newText
    ) {
        EditMessageText edit = new EditMessageText();
        edit.setMessageId(messageId);
        edit.setText(newText);
        edit.setChatId(chatId);
        return edit;
    }
}
