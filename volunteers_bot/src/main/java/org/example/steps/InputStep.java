package org.example.steps;

import org.example.builders.MessageBuilder;
import org.example.exceptions.EntityNotFoundException;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class InputStep extends ConversationStep {
    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(getPrepareMessageText())
                .sendMessage(chatHash.getId());
        int messageId = MessageUtil.sendMessage(sendMessage, sender);
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
