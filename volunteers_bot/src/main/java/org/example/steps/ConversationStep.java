package org.example.steps;

import lombok.Setter;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.KeyboardUtil;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

public abstract class ConversationStep {
    @Setter
    protected List<EConversationStep> eConversationStepList;

    public abstract void prepare(ChatHash chatHash, AbsSender sender);

    public abstract EConversationStep execute(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender
    );

    protected void finishStep(ChatHash chatHash, AbsSender sender, String text) {
        deleteKeyboard(chatHash, sender);
        sendFinishMessage(chatHash, sender, text);
    }

    protected EConversationStep handleIllegalUserAction(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender, String exceptionMessageText
    ) {
        MessageUtil.sendMessageText(exceptionMessageText, messageDto.getChatId(), sender);
        return chatHash.getEConversationStep();
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
