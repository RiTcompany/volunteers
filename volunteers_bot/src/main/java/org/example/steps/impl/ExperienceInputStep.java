package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
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
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String experience = messageDto.getData();
        ResultDto result = setExperience(chatHash.getId(), experience);
        if (result.isDone()) {
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT);
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setExperience(long chatId, String experience) {
//        TODO : нужно ли ограничивать ответ по длине? Вдруг нам закинуть письмо на 10000000000000000 символов
        saveExperience(chatId, experience);
        return new ResultDto(true);
    }

    private void saveExperience(long chatId, String experience) {
//        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
//        volunteer.setExperience(experience);
//        volunteerService.saveAndFlushVolunteer(volunteer);
    }
}