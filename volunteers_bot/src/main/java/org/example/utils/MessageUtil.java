package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.bots.Bot;
import org.example.builders.PageableInlineKeyboardMarkupBuilder;
import org.example.pojo.dto.KeyboardDto;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MessageUtil {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Для ID чата {} не удалось отправить сообщение. Причина: {}";
    private static final String EXCEPTION_MESSAGE_DOWNLOAD_FILE = "Не удалось скачать файл. Причина: {}";
    private static final String STORAGE_PATH = "C:\\Users\\User\\IdeaProjects\\volunteers\\volunteers_bot\\src\\main\\resources\\static\\";
    private static final String DOCUMENT_STORAGE_PATH = STORAGE_PATH.concat("documents\\");

    public static int sendMessageText(String text, long chatId, AbsSender sender) {
        SendMessage message = completeSendMessageText(text, chatId);
        return sendMessage(message, sender);
    }

    public static void editMessageText(long chatId, Integer messageId, String newText, AbsSender sender) {
        EditMessageText edit = completeEditMessageText(chatId, messageId, newText);
        editMessageText(edit, sender);
    }

    public static int sendMessageReplyMarkup(KeyboardDto keyboardDto, AbsSender sender) {
        SendMessage message = sendMessageWithKeyboard(keyboardDto);
        return sendMessage(message, sender);
    }

    public static void editMessageReplyMarkup(KeyboardDto keyboardDto, AbsSender sender) {
        EditMessageReplyMarkup editMessageReplyMarkup = editMessageWithKeyboard(keyboardDto);
        editMessageReplyMarkup(editMessageReplyMarkup, sender);
    }

    public static java.io.File downloadDocument(Document document, AbsSender sender) {
        GetFile getFile = completeGetFile(document.getFileId());
        File fileInfo = getFile(getFile, sender);
        if (fileInfo != null) {
            return downloadFile(fileInfo, sender);
        }

        return null;
    }

    public static int sendDocument(long chatId, java.io.File file, String text, AbsSender sender) {
        SendDocument document = completeSendDocument(chatId, new InputFile(file), text);
        return sendDocument(document, sender);
    }

    private static SendMessage sendMessageWithKeyboard(KeyboardDto keyboardDto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboard(keyboardDto);
        return MessageUtil.completeSendMessage(keyboardDto.getMessageText(), keyboardDto.getChatId(), inlineKeyboardMarkup);
    }

    private static EditMessageReplyMarkup editMessageWithKeyboard(KeyboardDto keyboardDto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboard(keyboardDto);
        return MessageUtil.completeEditMessageReplyMarkup(keyboardDto.getChatId(), keyboardDto.getMessageId(), inlineKeyboardMarkup);
    }

    private static InlineKeyboardMarkup inlineKeyboard(KeyboardDto keyboardDto) {
        return PageableInlineKeyboardMarkupBuilder.create().setPageNumber(keyboardDto.getPageNumber()).setButtonList(keyboardDto.getButtonDtoList()).build();
    }

    private static EditMessageReplyMarkup completeEditMessageReplyMarkup(long chatId, Integer messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setMessageId(messageId);
        edit.setReplyMarkup(inlineKeyboardMarkup);
        edit.setChatId(chatId);
        return edit;
    }

    private static SendMessage completeSendMessage(String text, long chatId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = completeSendMessageText(text, chatId);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    private static SendMessage completeSendMessageText(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        return message;
    }


    private static EditMessageText completeEditMessageText(long chatId, Integer messageId, String newText) {
        EditMessageText edit = new EditMessageText();
        edit.setMessageId(messageId);
        edit.setText(newText);
        edit.setChatId(chatId);
        return edit;
    }

    private static GetFile completeGetFile(String fileId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        return getFile;
    }

    private static SendDocument completeSendDocument(long chatId, InputFile inputFile, String text) {
        SendDocument document = new SendDocument();
        document.setDocument(inputFile);
        document.setChatId(chatId);
        document.setCaption(text);
        return document;
    }

    private static int sendMessage(SendMessage message, AbsSender sender) {
        try {
            message.enableHtml(true);
            return sender.execute(message).getMessageId();
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, message.getChatId(), e.getMessage());
        }

        return -1;
    }

    private static void editMessageText(EditMessageText edit, AbsSender sender) {
        try {
            edit.enableHtml(true);
            sender.execute(edit);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, edit.getChatId(), e.getMessage());
        }
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
            return ((Bot) sender).downloadFile(filePath, new java.io.File(STORAGE_PATH.concat(filePath)));
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_DOWNLOAD_FILE, e.getMessage());
        }

        return null;
    }

    private static int sendDocument(SendDocument sendDocument, AbsSender sender) {
        try {
            return sender.execute(sendDocument).getMessageId();
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, sendDocument.getChatId(), e.getMessage());
        }

        return -1;
    }
}
