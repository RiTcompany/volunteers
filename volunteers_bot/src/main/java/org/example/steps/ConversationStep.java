package org.example.steps;

import org.example.exceptions.EntityNotFoundException;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ConversationStep {
    public abstract void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException;

    public abstract int execute(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender
    ) throws EntityNotFoundException;

    protected abstract String getPrepareMessageText();

    protected abstract int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException;

    protected void sendFinishMessage(ChatHash chatHash, AbsSender sender, String text) {
        MessageUtil.sendMessageText(text, chatHash.getId(), sender);
    }

    protected int handleIllegalUserAction(
            MessageDto messageDto, AbsSender sender, String exceptionMessageText
    ) {
        MessageUtil.sendMessageText(exceptionMessageText, messageDto.getChatId(), sender);
        return -1;
    }
}
