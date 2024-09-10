package org.example.steps;

import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.AbstractException;
import org.example.exceptions.FileNotDownloadedException;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

public abstract class FileSendStep extends ConversationStep {
    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws AbstractException {
        ResultDto result = isValidFile(messageDto, sender);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        downloadFile(chatHash.getId(), messageDto, sender);
        return finishStep(chatHash, sender, getAnswerMessageText());
    }

    protected abstract ResultDto isValidFile(MessageDto messageDto, AbsSender sender);

    protected void downloadFile(long chatId, MessageDto messageDto, AbsSender sender) throws FileNotDownloadedException {
        File file = download(messageDto, sender);
        if (file != null) {
            saveDocument(chatId, file.getPath());
        } else {
            throw new FileNotDownloadedException("ChatId=" + chatId + " не удалось скачать");
        }
    }

    protected abstract File download(MessageDto messageDto, AbsSender sender);

    protected abstract void saveDocument(long chatId, String path);

    protected abstract String getAnswerMessageText();
}
