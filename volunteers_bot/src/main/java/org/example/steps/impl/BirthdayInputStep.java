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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BirthdayInputStep extends InputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Укажите вашу <b>дату рождения</b>: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваша дата рождения: <b>";
    private static final String EXCEPTION_MESSAGE_TEXT = "Неверный ввод даты рождения. Введите данные по шаблону <b>дд.мм.гггг</b>";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final int MAX_AGE = 122;

    public BirthdayInputStep(VolonteerRepository volonteerRepository) {
        super(volonteerRepository, PREPARE_MESSAGE_TEXT);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String birthday = messageDto.getData();
        boolean isCitySaved = setBirthday(chatHash, birthday);
        if (isCitySaved) {
            finishStep(chatHash, messageDto, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(birthday).concat("</b>"));
            return eConversationStepList.get(0);
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private boolean setBirthday(ChatHash chatHash, String birthdayStr) {
        try {
            Date birthdayDate = DATE_FORMAT.parse(birthdayStr);
            if (isValidBirthday(birthdayDate)) {
                Volonteer volonteer = getVolonteer(chatHash);
                volonteer.setBirthday(birthdayDate);
                saveAndFlushVolonteer(volonteer);
                return true;
            }
        } catch (ParseException ignored) {
        }

        log.error("Chat ID={} Incorrect birthday : {}", chatHash.getId(), birthdayStr);
        return false;
    }

    private boolean isValidBirthday(Date birthday) {
        Date today = new Date();

        Calendar calDate = Calendar.getInstance();
        calDate.setTime(birthday);
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);
        int yearsDifference = calToday.get(Calendar.YEAR) - calDate.get(Calendar.YEAR);

        return new Date().after(birthday) && yearsDifference < MAX_AGE;
    }
}
