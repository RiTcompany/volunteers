package org.example.steps;

import org.example.pojo.entities.ChatHash;
import org.example.repositories.VolonteerRepository;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class InputStep extends VolonteerConversationStep {
    public InputStep(VolonteerRepository volonteerRepository, String prepareMessageText) {
        super(volonteerRepository, prepareMessageText);
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendMessage(PREPARE_MESSAGE_TEXT, chatHash.getId(), sender);
        chatHash.setPrevBotMessageId(messageId);
    }
}
