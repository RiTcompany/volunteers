package org.example.steps.impl.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Parent;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ParentService;
import org.example.steps.InputStep;
import org.example.utils.DateUtil;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChildBirthdayInputStep extends InputStep {
    private final ParentService parentService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите <b>дату рождения</b> вашего ребёнка:";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final int MAX_AGE = 122;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessage(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String data) {
        Date birthdayDate = convertDate(data);

        if (birthdayDate == null) {
            return new ResultDto(false, "Некорректный формат даты. Введите данные по шаблону <b>дд.мм.гггг</b>");
        }

        if (!isValidBirthday(birthdayDate)) {
            return new ResultDto(false, "Вы ввели невозможную дату рождения. Введите свои настоящие данные");
        }

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String data) throws EntityNotFoundException {
        Parent parent = parentService.getByChatId(chatId);
        parent.setChildBirthday(convertDate(data));
        parentService.saveAndFlush(parent);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Дата рождения вашего ребёнка: <b>".concat(answer).concat("</b>");
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
}