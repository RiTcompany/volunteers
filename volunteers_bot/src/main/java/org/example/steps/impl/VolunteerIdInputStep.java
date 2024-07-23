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
public class VolunteerIdInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "С сайта https://волонтёрыпобеды.рф введите ваш <b>ID</b>:";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String volunteerId = messageDto.getData();
        ResultDto result = setVolunteerId(chatHash.getId(), volunteerId);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(volunteerId));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setVolunteerId(long chatId, String email) {
//        TODO : будем ли проверить верность id ???

        saveVolunteerId(chatId, email);
        return new ResultDto(true);
    }

    private void saveVolunteerId(long chatId, String volunteerId) {
//        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
//        volunteer.setVolunteerId(volunteerId);
//        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private String getAnswerMessageText(String id) {
        return "Ваш ID: <b>".concat(id).concat("</b>");
    }
}
