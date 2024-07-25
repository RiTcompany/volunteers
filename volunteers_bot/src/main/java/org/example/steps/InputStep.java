package org.example.steps;

import org.example.exceptions.EntityNotFoundException;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class InputStep extends ConversationStep {
    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendMessageText(getPrepareMessageText(), chatHash.getId(), sender);
        chatHash.setPrevBotMessageId(messageId);
    }

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
