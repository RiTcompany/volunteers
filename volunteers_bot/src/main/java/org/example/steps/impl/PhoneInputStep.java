package org.example.steps.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;
import org.example.steps.InputStep;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Pattern;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PhoneInputStep extends InputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваш <b>номер телефона</b>: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваш номер телефона: <b>";
    private static final String EXCEPTION_MESSAGE_TEXT = "Неверный номер телефона";
//    TODO : Нужна нормальная валидация
    private static final Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)[\\d\\- ]{7}$");

    public PhoneInputStep(VolonteerRepository volonteerRepository) {
        super(volonteerRepository, PREPARE_MESSAGE_TEXT);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String phone = messageDto.getData();
        boolean citySaved = setPhone(chatHash, phone);
        if (citySaved) {
            finishStep(chatHash, messageDto, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(phone).concat("</b>"));
            return eConversationStepList.get(0);
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private boolean setPhone(ChatHash chatHash, String phone) {
        boolean isValidPhone = isValidPhone(phone);
        if (isValidPhone) {
            Volonteer volonteer = getVolonteer(chatHash);
            volonteer.setPhone(phone);
            saveAndFlushVolonteer(volonteer);
            return true;
        }

        log.error("Chat ID={} Incorrect phone : {}", chatHash.getId(), phone);
        return false;
    }

    private boolean isValidPhone(String phone) {
        return pattern.matcher(phone).matches();
    }
}
