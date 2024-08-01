package org.example.steps;

import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EMessage;
import org.example.exceptions.EntityNotFoundException;
import org.example.utils.KeyboardUtil;
import org.example.utils.ValidUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ChoiceStep extends ConversationStep {
    protected static final String EXCEPTION_MESSAGE_TEXT = "Выберите один из выше предложенных вариантов";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        ResultDto result = isValidData(messageDto);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        deleteKeyboard(chatHash, sender);
        return finishStep(chatHash, sender, messageDto.getData());
    }

    protected abstract ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException;

    @Override
    protected void sendFinishMessage(ChatHash chatHash, AbsSender sender, String text) {
        super.sendFinishMessage(chatHash, sender, text);
    }

    protected void deleteKeyboard(ChatHash chatHash, AbsSender sender) {
        long chatId = chatHash.getId();
        int keyBoardMessageId = chatHash.getPrevBotMessageId();
        KeyboardUtil.cleanKeyboard(chatId, keyBoardMessageId, sender);
        chatHash.setDefaultPrevBotMessagePageNumber();
    }

    protected boolean isCallback(EMessage eMessage) {
        return ValidUtil.isCallback(eMessage);
    }
}
