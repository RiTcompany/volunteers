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
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class VolunteerIdInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "С сайта https://волонтёрыпобеды.рф введите ваш <b>ID</b>:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessage(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (!ValidUtil.containsDigitsOnly(data)) {
            return new ResultDto(false, "Номер должен состоять только из цифр");
        }

        return new ResultDto(true); // TODO : возможно надо запрашивать корректность номера с сайта (но тогда и просить ввести не надо)
    }

    @Override
    protected void saveData(long chatId, String data) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setVolunteerId(data);
        volunteerService.saveAndFlush(volunteer);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String id) {
        return "Ваш ID: <b>".concat(id).concat("</b>");
    }
}
