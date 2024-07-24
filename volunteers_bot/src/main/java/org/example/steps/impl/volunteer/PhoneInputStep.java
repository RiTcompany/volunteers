package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhoneInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваш <b>номер телефона</b>:";
    //    TODO : Проверить валидацию
    private static final Pattern pattern = Pattern.compile("^((8|\\+7)\\s?)?((\\(\\d{3}\\))|(\\d{3}))(\\s|-)?(\\d{3}-?\\d{2}-?\\d{2})$");

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (!pattern.matcher(data).matches()) {
            return new ResultDto(false, "Неверный формат номера телефона. Вы можете ввести свой номер в виде +7(xxx)xxx-xx-xx");
        }

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String data) {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setPhone(data);
        volunteerService.saveAndFlush(volunteer);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        saveData(chatHash.getId(), data);
        cleanPreviousMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш номер телефона: <b>".concat(answer).concat("</b>");
    }
}
