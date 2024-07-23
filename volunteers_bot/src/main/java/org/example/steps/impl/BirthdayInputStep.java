package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthdayInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите вашу <b>дату рождения</b>:";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final int MAX_AGE = 122;
    private static final int MAJORITY_AGE = 18;

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        Date date = convertDate(messageDto.getData());
        ResultDto result = setBirthday(chatHash.getId(), date);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(DATE_FORMAT.format(date)));
            if (isMinor(date)) {
                return 0;
            }

            return 1;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private Date convertDate(String birthdayStr) {
        try {
            return DATE_FORMAT.parse(birthdayStr);
        } catch (ParseException ignored) {
            return null;
        }
    }

    private ResultDto setBirthday(long chatId, Date birthdayDate) {
        if (birthdayDate == null) {
            return new ResultDto(false, "Некорректный формат даты. Введите данные по шаблону <b>дд.мм.гггг</b>");
        }

        if (!isValidBirthday(birthdayDate)) {
            log.error("Chat ID={} Incorrect birthday : {}", chatId, birthdayDate);
            return new ResultDto(false, "Вы ввели невозможную дату рождения. Введите свои настоящие данные");
        }

        saveBirthday(chatId, birthdayDate);
        return new ResultDto(true);
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

    private void saveBirthday(long chatId, Date date) {
        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
        volunteer.setBirthday(date);
        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваша дата рождения: <b>".concat(answer).concat("</b>");
    }
}
