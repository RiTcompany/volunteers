package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.AbstractException;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class EducationalSpecialtyInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите вашу специальность";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException {
        volunteerService.saveEducationSpeciality(chatHash.getId(), data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    @Override
    protected ResultDto isValidData(String data) {
        return ValidUtil.isValidShortString(data);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваша специальность: <b>".concat(answer).concat("</b>");
    }
}
