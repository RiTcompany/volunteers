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
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String phone = messageDto.getData();
        ResultDto result = setPhone(chatHash.getId(), phone);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(phone));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setPhone(long chatId, String phone) {
        boolean isValidPhone = isValidPhone(phone);
        if (isValidPhone) {
            savePhone(chatId, phone);
            return new ResultDto(true);
        }

        log.error("Chat ID={} Incorrect phone : {}", chatId, phone);
        return new ResultDto(false, "Неверный формат номера телефона. Вы можете ввести свой номер в виде +7(xxx)xxx-xx-xx");
    }

    private boolean isValidPhone(String phone) {
        return pattern.matcher(phone).matches();
    }

    private void savePhone(long chatId, String phone) {
        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
        volunteer.setPhone(phone);
        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш номер телефона: <b>".concat(answer).concat("</b>");
    }
}
