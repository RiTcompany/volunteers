package org.example.steps;

import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.FileNotDownloadedException;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class FileSendStep extends ConversationStep {
    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws FileNotDownloadedException, EntityNotFoundException {
        ResultDto result = isValidFile(messageDto, sender);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        downloadFile(chatHash.getId(), messageDto, sender);
        return finishStep(chatHash, sender, getAnswerMessageText());

    }

    protected abstract ResultDto isValidFile(MessageDto messageDto, AbsSender sender);

    protected abstract void downloadFile(long chatId, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException, FileNotDownloadedException;

    protected abstract String getAnswerMessageText();
}
