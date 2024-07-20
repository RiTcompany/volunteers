package org.example.steps.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
import org.example.steps.InputStep;
import org.example.utils.CityUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CityInputStep extends InputStep {
    private final VolonteerService volonteerService;
    @Getter(AccessLevel.PROTECTED)
    private static final String PREPARE_MESSAGE_TEXT = "Мероприятия проходит очно в Санкт-Петербурге. Если вы подтверждаете, что сможете участвовать в них, то продолжите регистрацию\n\nВведите ваш <b>город</b>: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваш город: <b>";
    private static final String EXCEPTION_MESSAGE_TEXT = "Такого города не существует в РФ. Проверьте нет ли ошибок в вашем ответе и повторите его";

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String city = CityUtil.setCityRegister(messageDto.getData());
        boolean isCitySaved = setCity(chatHash.getId(), city);
        if (isCitySaved) {
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(city).concat("</b>"));
            return eConversationStepList.get(0);
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private boolean setCity(long chatId, String city) {
        if (!isValidCity(city)) {
            log.error("Chat ID={} incorrect city: {}", chatId, city);
            return false;
        }

        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setCity(city);
        volonteerService.saveAndFlushVolonteer(volonteer);
        return true;
    }

    private boolean isValidCity(String city) {
        return CityUtil.isExistsRussianCity(city);
    }
}
