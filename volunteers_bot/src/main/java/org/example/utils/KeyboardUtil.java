package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.builders.PageableInlineKeyboardMarkupBuilder;
import org.example.enums.PageMoveEnum;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.KeyboardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
public class KeyboardUtil {
    public static void cleanKeyboard(long chatId, int messageId, AbsSender sender) {
        try {
            EditMessageReplyMarkup edit = completeEditMessageReplyMarkup(chatId, messageId);
            sender.execute(edit);
        } catch (TelegramApiException e) {
            log.warn("В чате ID={} нет клавиатуры для её удаления", chatId);
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
        MessageUtil.editMessageReplyMarkup(keyboardDto, sender);
    }

    private static int getPageCount(List<ButtonDto> buttonDtoList) {
        return PageableInlineKeyboardMarkupBuilder.getPageCount(buttonDtoList);
    }
}
