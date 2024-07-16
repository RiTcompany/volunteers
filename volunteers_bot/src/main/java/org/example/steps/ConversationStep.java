package org.example.steps;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.KeyboardUtil;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ConversationStep {
    protected final String PREPARE_MESSAGE_TEXT;

    @Setter
    protected List<EConversationStep> eConversationStepList;

    public abstract void prepare(ChatHash chatHash, AbsSender sender);

    public abstract EConversationStep execute(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender
    );

    protected void finishStep(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender, String editMessageText
    ) {
        deleteKeyboard(chatHash, sender);
        editMessageText(messageDto, sender, editMessageText);
    }

    protected EConversationStep handleIllegalUserAction(
            ChatHash chatHash, MessageDto messageDto, AbsSender sender, String exceptionMessageText
    ) {
        MessageUtil.sendMessage(exceptionMessageText, messageDto.getChatId(), sender);
        return chatHash.getEConversationStep();
    }

    private void deleteKeyboard(ChatHash chatHash, AbsSender sender) {
        long chatId = chatHash.getId();
        int keyBoardMessageId = chatHash.getPrevBotMessageId();
        KeyboardUtil.cleanKeyboard(chatId, keyBoardMessageId, sender);
    }

    private void editMessageText(MessageDto messageDto, AbsSender sender, String editMessageText) {
        MessageUtil.editMessage(
                messageDto.getChatId(),
                messageDto.getPrevBotMessageId(),
                editMessageText,
                sender
        );
    }
}
