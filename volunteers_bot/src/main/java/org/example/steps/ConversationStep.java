package org.example.steps;

import org.example.builders.MessageBuilder;
import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.exceptions.AbstractException;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ConversationStep {
    public abstract void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException;

    public abstract int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws AbstractException;

    protected abstract int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException;

    protected void sendFinishMessage(ChatHash chatHash, AbsSender sender, String text) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(text)
                .sendMessage(chatHash.getId());
        MessageUtil.sendMessage(sendMessage, sender);
    }

    protected int handleIllegalUserAction(
            MessageDto messageDto, AbsSender sender, String exceptionMessageText
    ) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(exceptionMessageText)
                .sendMessage(messageDto.getChatId());
        MessageUtil.sendMessage(sendMessage, sender);
        return -1;
    }
}
