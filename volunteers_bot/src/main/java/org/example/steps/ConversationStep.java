package org.example.steps;

import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.KeyboardUtil;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ConversationStep {
    public abstract void prepare(ChatHash chatHash, AbsSender sender);

    public abstract int execute(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender
    );

    protected abstract String getPREPARE_MESSAGE_TEXT();

    protected void finishStep(ChatHash chatHash, AbsSender sender, String text) {
        deleteKeyboard(chatHash, sender);
        sendFinishMessage(chatHash, sender, text);
    }

    protected int handleIllegalUserAction(
            MessageDto messageDto, AbsSender sender, String exceptionMessageText
    ) {
        MessageUtil.sendMessageText(exceptionMessageText, messageDto.getChatId(), sender);
        return -1;
    }

    private void deleteKeyboard(ChatHash chatHash, AbsSender sender) {
        long chatId = chatHash.getId();
        int keyBoardMessageId = chatHash.getPrevBotMessageId();
        KeyboardUtil.cleanKeyboard(chatId, keyBoardMessageId, sender);
    }

    private void sendFinishMessage(ChatHash chatHash, AbsSender sender, String text) {
        MessageUtil.sendMessageText(text, chatHash.getId(), sender);
    }
}
