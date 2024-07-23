package org.example.steps;

import org.example.pojo.entities.ChatHash;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

public abstract class DocumentStep extends ConversationStep {
    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendDocument(chatHash.getId(), getFILE(), getPREPARE_MESSAGE_TEXT(), sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    protected abstract File getFILE();
}
