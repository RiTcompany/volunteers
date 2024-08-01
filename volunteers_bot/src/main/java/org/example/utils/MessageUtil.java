package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.bots.TGLongPoolingBot;
import org.example.builders.MessageBuilder;
import org.example.builders.PageableInlineKeyboardMarkupBuilder;
import org.example.dto.KeyboardDto;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MessageUtil {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Для ID чата {} не удалось отправить сообщение. Причина: {}";
    private static final String EXCEPTION_MESSAGE_DOWNLOAD_FILE = "Не удалось скачать файл. Причина: {}";
    private static final String STORAGE_PATH = "C:\\Users\\User\\IdeaProjects\\volunteers\\volunteers_bot\\src\\main\\resources\\static\\";

    public static int sendMessageText(String text, long chatId, AbsSender sender) {
        return MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(text)
                        .sendMessage(chatId),
                sender
        );
    }

    public static void editMessageReplyMarkup(KeyboardDto keyboardDto, AbsSender sender) {
        EditMessageReplyMarkup editMessageReplyMarkup = editMessageWithKeyboard(keyboardDto);
        editMessageReplyMarkup(editMessageReplyMarkup, sender);
    }

    public static java.io.File downloadFile(String fileId, AbsSender sender) {
        GetFile getFile = completeGetFile(fileId);
        File fileInfo = getFile(getFile, sender);
        if (fileInfo != null) {
            return downloadFile(fileInfo, sender);
        }

        return null;
    }

    public static int sendFile(long chatId, java.io.File file, String text, AbsSender sender) {
        return MessageUtil.sendDocument(
                MessageBuilder.create()
                        .setFile(file)
                        .setText(text)
                        .sendDocument(chatId),
                sender
        );
    }

    public static int sendMessage(SendMessage message, AbsSender sender) {
        try {
            message.enableHtml(true);
            message.disableWebPagePreview();
            return sender.execute(message).getMessageId();
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, message.getChatId(), e.getMessage());
        }

        return -1;
    }

    public static int sendDocument(SendDocument sendDocument, AbsSender sender) {
        try {
            return sender.execute(sendDocument).getMessageId();
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, sendDocument.getChatId(), e.getMessage());
        }

        return -1;
    }


    private static EditMessageReplyMarkup editMessageWithKeyboard(KeyboardDto keyboardDto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboard(keyboardDto);
        return MessageUtil.completeEditMessageReplyMarkup(
                keyboardDto.getChatId(), keyboardDto.getMessageId(), inlineKeyboardMarkup
        );
    }

    private static InlineKeyboardMarkup inlineKeyboard(KeyboardDto keyboardDto) {
        return PageableInlineKeyboardMarkupBuilder.create()
                .setPageNumber(keyboardDto.getPageNumber())
                .setButtonList(keyboardDto.getButtonDtoList())
                .build();
    }

    private static EditMessageReplyMarkup completeEditMessageReplyMarkup(
            long chatId, Integer messageId, InlineKeyboardMarkup inlineKeyboardMarkup
    ) {
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setMessageId(messageId);
        edit.setReplyMarkup(inlineKeyboardMarkup);
        edit.setChatId(chatId);
        return edit;
    }

    private static GetFile completeGetFile(String fileId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        return getFile;
    }

    private static void editMessageReplyMarkup(EditMessageReplyMarkup edit, AbsSender sender) {
        try {
            sender.execute(edit);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, edit.getChatId(), e.getMessage());
        }
    }

    private static File getFile(GetFile getFile, AbsSender sender) {
        try {
            return sender.execute(getFile);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_DOWNLOAD_FILE, e.getMessage());
        }

        return null;
    }

    private static java.io.File downloadFile(File fileInfo, AbsSender sender) {
        try {
            String filePath = fileInfo.getFilePath();
            return ((TGLongPoolingBot) sender).downloadFile(
                    filePath, new java.io.File(STORAGE_PATH.concat(filePath))
            );
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_DOWNLOAD_FILE, e.getMessage());
        }

        return null;
    }
}
