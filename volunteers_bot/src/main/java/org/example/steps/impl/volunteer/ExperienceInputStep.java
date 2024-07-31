package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.EntityNotFoundException;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExperienceInputStep  extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Расскажите, о своём волонтёрском опыте";
    private static final String ANSWER_MESSAGE_TEXT = "Спасибо за ваш ответ";

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
//        TODO : нужно ли ограничивать ответ по длине
        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String experience) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setExperience(experience);
        volunteerService.saveAndFlush(volunteer);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, ANSWER_MESSAGE_TEXT);
        return 0;
    }
}