package org.example.utils;

import org.example.pojo.dto.ButtonDto;
import org.example.builders.InlineKeyboardBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class KeyboardUtil {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardUtil.class);

    public static InlineKeyboardBuilder createKeyboardBuilder(
            long chatId, List<ButtonDto> buttonDtoList, String messageText
    ) {
        InlineKeyboardBuilder inlineKeyboardBuilder = InlineKeyboardBuilder.create(String.valueOf(chatId));
        buttonDtoList.forEach(inlineKeyboardBuilder::addButton);
        inlineKeyboardBuilder.setMessageText(messageText);

        return inlineKeyboardBuilder;
    }

    public static void cleanKeyboard(long chatId, Integer keyboardMessageId, AbsSender sender) {
        try {
            EditMessageReplyMarkup edit = completeEditMessageReplyMarkup(chatId, keyboardMessageId);
            sender.execute(edit);
        } catch (TelegramApiException e) {
            logger.warn("В чате ID={} нет клавиатуры для её удаления", chatId); // TODO : надо бы наверное глядеть, если ли клава и только тогда удалять
        }
    }

    private static EditMessageReplyMarkup completeEditMessageReplyMarkup(
            long chatId, Integer keyboardMessageId
    ) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(keyboardMessageId);

        return editMessageReplyMarkup;
    }
}
