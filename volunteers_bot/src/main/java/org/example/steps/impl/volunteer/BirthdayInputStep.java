package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.example.utils.DateUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        Date date = convertDate(data);
        if (date == null) {
            return new ResultDto(false, "Некорректный формат даты. Введите данные по шаблону <b>дд.мм.гггг</b>");
        }

        if (!isValidBirthday(date)) {
            return new ResultDto(false, "Вы ввели невозможную дату рождения. Введите свои настоящие данные");
        }

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String data) {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setBirthday(convertDate(data));
        volunteerService.saveAndFlush(volunteer);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        saveData(chatHash.getId(), data);
        cleanPreviousMessage(chatHash, sender, getAnswerMessageText(data));
        return isMinor(convertDate(data)) ? 0 : 1;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваша дата рождения: <b>".concat(answer).concat("</b>");
    }

    private Date convertDate(String birthdayStr) {
        try {
            return DATE_FORMAT.parse(birthdayStr);
        } catch (ParseException ignored) {
            return null;
        }
    }

    private boolean isValidBirthday(Date birthday) {
        int yearCount = DateUtil.getYearCountByDate(birthday);
        return new Date().after(birthday) && yearCount < MAX_AGE;
    }

    private boolean isMinor(Date birthday) {
        int yearCount = DateUtil.getYearCountByDate(birthday);
        return yearCount < MAJORITY_AGE;
    }
}
