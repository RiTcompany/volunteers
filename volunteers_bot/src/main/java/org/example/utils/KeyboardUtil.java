package org.example.utils;

import org.example.builders.PageableInlineKeyboardBuilder;
import org.example.enums.PageMoveEnum;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.KeyboardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class KeyboardUtil {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardUtil.class);

    public static SendMessage sendMessageWithKeyboard(KeyboardDto keyboardDto) {
        return pageableInlineKeyboardBuilder(keyboardDto)
                .buildSendMessage(keyboardDto.getChatId());
    }

    public static EditMessageReplyMarkup editMessageWithKeyboard(KeyboardDto keyboardDto) {
        return pageableInlineKeyboardBuilder(keyboardDto)
                .buildEditMessageReplyMarkup(keyboardDto.getChatId(), keyboardDto.getMessageId());
    }

    public static void cleanKeyboard(long chatId, int messageId, AbsSender sender) {
        try {
            EditMessageReplyMarkup edit = completeEditMessageReplyMarkup(chatId, messageId);
            sender.execute(edit);
        } catch (TelegramApiException e) {
            logger.warn("В чате ID={} нет клавиатуры для её удаления", chatId); // TODO : надо бы глядеть, если ли клава и только тогда удалять
        }
    }

    public static int movePage(PageMoveEnum pageMoveEnum, KeyboardDto keyboardDto, AbsSender sender) {
        int newPageNumber = switch (pageMoveEnum) {
            case NEXT -> incrementPageNumber(
                    keyboardDto.getPageNumber(),
                    getPageCount(keyboardDto.getButtonDtoList())
            );
            case PREV -> decrementPageNumber(
                    keyboardDto.getPageNumber(),
                    getPageCount(keyboardDto.getButtonDtoList())
            );
        };

        keyboardDto.setPageNumber(newPageNumber);
        setNewPage(keyboardDto, sender);
        return newPageNumber;
    }

    private static EditMessageReplyMarkup completeEditMessageReplyMarkup(
            long chatId, Integer keyboardMessageId
    ) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(keyboardMessageId);

        return editMessageReplyMarkup;
    }

    private static PageableInlineKeyboardBuilder pageableInlineKeyboardBuilder(KeyboardDto keyboardDto) {
        PageableInlineKeyboardBuilder builder = PageableInlineKeyboardBuilder.create();
        builder.setMessageText(keyboardDto.getMessageText());
        builder.addButtonList(keyboardDto.getButtonDtoList(), keyboardDto.getPageNumber());

        return builder;
    }

    private static int incrementPageNumber(int pageNumber, int pageCount) {
        int newPrevBotMessagePageNumber = pageNumber + 1;
        if (newPrevBotMessagePageNumber == pageCount) {
            return 0;
        }

        return newPrevBotMessagePageNumber;
    }

    private static int decrementPageNumber(int pageNumber, int pageCount) {
        int newPrevBotMessagePageNumber = pageNumber - 1;
        if (newPrevBotMessagePageNumber < 0) {
            return pageCount - 1;
        }

        return newPrevBotMessagePageNumber;
    }

    private static void setNewPage(KeyboardDto keyboardDto, AbsSender sender) {
        EditMessageReplyMarkup editMessageReplyMarkup = KeyboardUtil.editMessageWithKeyboard(keyboardDto);
        MessageUtil.editMessageReplyMarkup(editMessageReplyMarkup, sender);
    }

    private static int getPageCount(List<ButtonDto> buttonDtoList) {
        return PageableInlineKeyboardBuilder.getPageCount(buttonDtoList);
    }
}
