package org.example.steps;

import org.example.pojo.entities.ChatHash;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class InputStep extends ConversationStep {
    protected abstract String getPREPARE_MESSAGE_TEXT();

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendMessageText(getPREPARE_MESSAGE_TEXT(), chatHash.getId(), sender);
        chatHash.setPrevBotMessageId(messageId);
    }
}
