package org.example.steps;

import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.enums.EConversationStep;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CityInputStep extends ConversationStep {
    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        MessageUtil.sendMessage("Введите ваш город:", chatHash.getId(), sender);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        return null;
    }
}
