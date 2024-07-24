package org.example.steps;

import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

public abstract class DocumentStep extends ConversationStep {
    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendDocument(chatHash.getId(), getFile(), getPrepareMessageText(), sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        Document document = messageDto.getDocument();
        ResultDto result = downloadDocument(document, sender);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        return finishStep(chatHash, sender, getAnswerMessageText());
    }

    protected abstract File getFile();

    protected abstract ResultDto downloadDocument(Document document, AbsSender sender);

    protected abstract String getAnswerMessageText();
}
