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
public class EmailInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваш <b>Email</b>:";
    private static final Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (!pattern.matcher(data).matches()) {
            return new ResultDto(false, "Некорректный email. Попробуйте другой");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        volunteerService.saveEmail(chatHash.getId(), data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String email) {
        return "Ваш email: <b>".concat(email).concat("</b>");
    }
}
