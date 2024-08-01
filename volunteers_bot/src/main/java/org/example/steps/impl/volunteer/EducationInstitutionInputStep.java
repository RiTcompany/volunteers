package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Volunteer;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class EducationInstitutionInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваше <b>учебное заведение</b>:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessage(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String educationInstitution) {
        if (educationInstitution.isEmpty()) {
            return new ResultDto(false, "Такого учебного заведения не существует");
        } // TODO : нужна валидация, надо найти список

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String data) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setEducationInstitution(data);
        volunteerService.saveAndFlush(volunteer);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше учебное заведение: <b>".concat(answer).concat("</b>");
    }
}
