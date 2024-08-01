package org.example.steps;

import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class InputStep extends ConversationStep {
    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        String data = messageDto.getData();
        ResultDto result = isValidData(data);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }


        saveData(chatHash.getId(), data);
        return finishStep(chatHash, sender, data);
    }

    protected abstract ResultDto isValidData(String data);

    protected abstract void saveData(long chatId, String data) throws EntityNotFoundException;
}
