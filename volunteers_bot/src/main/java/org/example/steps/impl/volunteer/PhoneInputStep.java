package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhoneInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваш <b>номер телефона</b>:";
    private static final Pattern pattern = Pattern.compile("^((8|\\+7)\\s?)?((\\(\\d{3}\\))|(\\d{3}))(\\s|-)?(\\d{3}-?\\d{2}-?\\d{2})$");

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (!pattern.matcher(data).matches()) {
            return new ResultDto(false, "Неверный формат номера телефона. Вы можете ввести свой номер в виде +7(xxx)xxx-xx-xx");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        volunteerService.savePhone(chatHash.getId(), formatPhone(data));
        sendFinishMessage(chatHash, sender, getAnswerMessageText(formatPhone(data)));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш номер телефона: <b>".concat(answer).concat("</b>");
    }

    private String formatPhone(String phone) {
        String newPhone = phone.replaceAll("[()\\-\\s+]", "");
        return "8".concat(newPhone.substring(newPhone.length() - 10));
    }
}
