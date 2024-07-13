package org.example.steps;

import lombok.Setter;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversationStep;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

public abstract class ConversationStep {
    @Setter
    protected List<EConversationStep> eConversationStepList;

    public abstract void prepare(ChatHash chatHash, AbsSender sender);

    public abstract EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender);
}
