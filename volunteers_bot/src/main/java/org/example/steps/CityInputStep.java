package org.example.steps;

import org.example.enums.ECity;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CityInputStep extends VolonteerConversationStep {
    private final String PREPARE_MESSAGE_TEXT = "Введите ваш город: ";
    private final String CITY_MESSAGE_TEXT_TEMPLATE = "Ваш город: ";
    private final String EXCEPTION_MESSAGE_TEXT = "Такого города не существует в РФ. Проверьте нет ли ошибок в вашем ответе и повторите его";

    public CityInputStep(VolonteerRepository volonteerRepository) {
        super(volonteerRepository);
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendMessage(PREPARE_MESSAGE_TEXT, chatHash.getId(), sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String city = messageDto.getData();
        boolean citySaved = setCity(chatHash.getId(), city);
        if (citySaved) {
            finishStep(chatHash, messageDto, sender, CITY_MESSAGE_TEXT_TEMPLATE.concat(city));
            return eConversationStepList.get(0);
        } else {
            return executeIllegalWay(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    private boolean setCity(long chatId, String city) {
        if (!isValidCity(city)) return false;

        Volonteer volonteer = findVolonteerByChatId(chatId);
        volonteer.setCity(city);
        saveAndFlushVolonteer(volonteer);
        return true;
    }

    private boolean isValidCity(String city) {
        return true;
    }
}
