package org.example.steps;

import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class FileSendStep extends ConversationStep {
    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        ResultDto result = isValidFile(messageDto, sender);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        saveFile(chatHash.getId(), messageDto, sender);
        return finishStep(chatHash, sender, getAnswerMessageText());
    }

    protected abstract ResultDto isValidFile(MessageDto messageDto, AbsSender sender);

    protected abstract void saveFile(long chatId, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException;

    protected abstract String getAnswerMessageText();
}
