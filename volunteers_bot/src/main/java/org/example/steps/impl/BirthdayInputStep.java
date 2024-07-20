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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class BirthdayInputStep extends InputStep {
    private final VolonteerService volonteerService;
    @Getter(AccessLevel.PROTECTED)
    private static final String PREPARE_MESSAGE_TEXT = "Укажите вашу <b>дату рождения</b>: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваша дата рождения: <b>";
    private static final String EXCEPTION_MESSAGE_TEXT = "Неверный ввод даты рождения. Введите данные по шаблону <b>дд.мм.гггг</b>";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final int MAX_AGE = 122;
    private static final int MAJORITY_AGE = 18;

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String birthday = messageDto.getData();
        Date date = convertDate(birthday);
        if (date != null) {
            boolean isCitySaved = setBirthday(chatHash.getId(), date);
            if (isCitySaved) {
                finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(birthday).concat("</b>"));
                if (isMinor(date)) {
                    return eConversationStepList.get(0);
                }

                return eConversationStepList.get(1);
            }
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private Date convertDate(String birthdayStr) {
        try {
            return DATE_FORMAT.parse(birthdayStr);
        } catch (ParseException ignored) {
            return null;
        }
    }

    private boolean setBirthday(long chatId, Date birthdayDate) {
        if (isValidBirthday(birthdayDate)) {
            Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
            volonteer.setBirthday(birthdayDate);
            volonteerService.saveAndFlushVolonteer(volonteer);
            return true;
        }

        log.error("Chat ID={} Incorrect birthday : {}", chatId, birthdayDate.toString());
        return false;
    }

    private boolean isValidBirthday(Date birthday) {
        int yearCount = getYearCount(birthday);
        return new Date().after(birthday) && yearCount < MAX_AGE;
    }

    private boolean isMinor(Date birthday) {
        int yearCount = getYearCount(birthday);
        return yearCount < MAJORITY_AGE;
    }

    private int getYearCount(Date date) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");
        Calendar calDate = Calendar.getInstance(timeZone);
        calDate.setTime(date);
        Calendar calToday = Calendar.getInstance(timeZone);
        calToday.setTime(new Date());

        int yearsDifference = calToday.get(Calendar.YEAR) - calDate.get(Calendar.YEAR);
        if (!wasBirthdayThisYear(calDate, calToday)) {
            yearsDifference--;
        }

        return yearsDifference;
    }

    private boolean wasBirthdayThisYear(Calendar birthday, Calendar today) {
        if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
            return false;
        }

        if (today.get(Calendar.MONTH) > birthday.get(Calendar.MONTH)) {
            return true;
        }

        return today.get(Calendar.DAY_OF_MONTH) >= birthday.get(Calendar.DAY_OF_MONTH);
    }
}
