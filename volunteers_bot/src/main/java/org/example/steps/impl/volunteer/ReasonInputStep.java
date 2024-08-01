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
public class ReasonInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Расскажите, почему вы решили стать волонтёром";
    private static final String ANSWER_MESSAGE_TEXT = "Спасибо за ваш ответ";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessage(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (ValidUtil.isLongDescriptionText(data)) {
            String exceptionMessage = ValidUtil.getExceptionMessageText(ValidUtil.MAX_DESCRIPTION_TEXT_LENGTH);
            return new ResultDto(false, exceptionMessage);
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, ANSWER_MESSAGE_TEXT);
        return 0;
    }

    @Override
    protected void saveData(long chatId, String data) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setReason(data);
        volunteerService.saveAndFlush(volunteer);
    }
}
